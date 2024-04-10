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
import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaDTO;
import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class TransferenciaFacade {

    @Autowired
    TransferenciaService transferenciaService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    ContaService contaService;
    @Autowired
    NotificacaoService notificacaoService;

    @Transactional
    public TransferenciaDTO efetuarTransferencia(TransferenciaRequestDTO request) throws Exception {
        var cliente = clienteService.buscarCliente(request.getIdCliente());

        var lock = new ReentrantLock();
        lock.lock();
        try {
            var contaOrigem = contaService.buscarConta(request.getConta().getIdOrigem());

            verificarContaAtiva(contaOrigem);
            verificarSaldoDisponivel(contaOrigem, request.getValor());
            verificarLimiteDiario(contaOrigem, request.getValor());

            var transferenciaDTO = transferenciaService.salvarTransaferencia(request);

            enviarNotificacao(request);
            atualizarSaldo(request, cliente);
            return transferenciaDTO;
        } finally {
            lock.unlock();
        }
    }

    private void atualizarSaldo(TransferenciaRequestDTO request, ClienteResponseDTO cliente) throws ContaException {
        contaService.atualizarSaldo(SaldoRequestDTO.builder()
                .nomeDestino(cliente.getNome())
                .valor(request.getValor())
                .conta(SaldoRequestDTO.Conta.builder()
                        .idDestino(request.getConta().getIdDestino())
                        .idOrigem(request.getConta().getIdOrigem())
                        .build())
                .build());
    }

    private void enviarNotificacao(TransferenciaRequestDTO request) throws NotificacaoException {
        var notificacao = prepararNotificacao(request);
        notificacaoService.enviarNotificacao(notificacao);
    }

    private NotificacaoRequestDTO prepararNotificacao(TransferenciaRequestDTO request) {
        return NotificacaoRequestDTO.builder()
                .valor(request.getValor())
                .conta(
                        NotificacaoRequestDTO.Conta.builder()
                                .idOrigem(request.getConta().getIdOrigem())
                                .idDestino(request.getConta().getIdDestino())
                                .build()
                )
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
