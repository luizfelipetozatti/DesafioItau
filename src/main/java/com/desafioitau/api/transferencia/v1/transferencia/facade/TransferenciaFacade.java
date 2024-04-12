package com.desafioitau.api.transferencia.v1.transferencia.facade;

import com.desafioitau.api.transferencia.exceptions.conta.exception.*;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.v1.cliente.service.ClienteService;
import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.v1.conta.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.v1.conta.service.ContaService;
import com.desafioitau.api.transferencia.v1.notificacao.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.v1.notificacao.service.NotificacaoService;
import com.desafioitau.api.transferencia.v1.transferencia.model.StatusTransferenciaEnum;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaProducer;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class TransferenciaFacade {

    @Autowired
    TransferenciaService transferenciaService;
    @Autowired
    private TransferenciaProducer transferenciaProducer;
    @Autowired
    ClienteService clienteService;
    @Autowired
    ContaService contaService;
    @Autowired
    NotificacaoService notificacaoService;

    public TransferenciaDTO enviarTransferencia(TransferenciaRequestDTO request) {
        var transferenciaDTO = transferenciaService.salvarTransferencia(request);
        transferenciaProducer.enviarTransferencia(transferenciaDTO);
        return transferenciaDTO;
    }

    public void consumirTransferencia(TransferenciaDTO transferenciaDTO) throws Exception {
        var cliente = clienteService.buscarCliente(transferenciaDTO.getIdCliente());

        var lock = new ReentrantLock();
        lock.lock();
        try {
            var contaOrigem = contaService.buscarConta(transferenciaDTO.getConta().getIdOrigem());

            verificarContaAtiva(contaOrigem);
            verificarSaldoDisponivel(contaOrigem, transferenciaDTO.getValor());
            verificarLimiteDiario(contaOrigem, transferenciaDTO.getValor());

            enviarNotificacao(transferenciaDTO);
            atualizarSaldo(transferenciaDTO, cliente);

            transferenciaService.atualizarStatusTransferencia(transferenciaDTO.getIdTransferencia(), StatusTransferenciaEnum.PROCESSADA);
        } finally {
            lock.unlock();
        }
    }

    private void enviarNotificacao(TransferenciaDTO transferenciaDTO) throws NotificacaoException {
        var notificacao = prepararNotificacao(transferenciaDTO);
        notificacaoService.enviarNotificacao(notificacao);
    }

    private void atualizarSaldo(TransferenciaDTO transferenciaDTO, ClienteResponseDTO cliente) throws ContaException {
        var saldoRequest = prepararSaldo(transferenciaDTO, cliente);
        contaService.atualizarSaldo(saldoRequest);
    }

    private SaldoRequestDTO prepararSaldo(TransferenciaDTO transferenciaDTO, ClienteResponseDTO cliente) {
        return SaldoRequestDTO.builder()
                .nomeDestino(cliente.getNome())
                .valor(transferenciaDTO.getValor())
                .conta(SaldoRequestDTO.Conta.builder()
                        .idDestino(transferenciaDTO.getConta().getIdDestino())
                        .idOrigem(transferenciaDTO.getConta().getIdOrigem())
                        .build())
                .build();
    }

    private NotificacaoRequestDTO prepararNotificacao(TransferenciaDTO transferenciaDTO) {
        return NotificacaoRequestDTO.builder()
                .valor(transferenciaDTO.getValor())
                .conta(NotificacaoRequestDTO.Conta.builder()
                        .idOrigem(transferenciaDTO.getConta().getIdOrigem())
                        .idDestino(transferenciaDTO.getConta().getIdDestino())
                        .build())
                .build();
    }

    private void verificarLimiteDiario(ContaResponseDTO conta, double valor) throws ContaException {
        if (conta.getLimiteDiario() == 0) {
            log.error("Conta {} com limite diario zerado", conta.getId());
            throw new ContaLimiteDiarioZeradoException();
        }

        if (conta.getLimiteDiario() < valor) {
            log.error(String.format("Conta %s com limite diario inferior ao valor %s", conta.getId(), valor));
            throw new ContaLimiteDiarioInsuficienteException();
        }
    }

    private void verificarSaldoDisponivel(ContaResponseDTO conta, double valor) throws ContaException {
        if (conta.getSaldo() < valor) {
            log.error("Conta {} com saldo indisponivel", conta.getId());
            throw new ContaSaldoIndisponivelException();
        }
    }

    private void verificarContaAtiva(ContaResponseDTO conta) throws ContaInativaException {
        if (!conta.isAtivo()) {
            log.error("Conta inativa com ID: {}", conta.getId());
            throw new ContaInativaException();
        }
    }
}
