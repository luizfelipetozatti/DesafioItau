package com.desafioitau.api.transferencia.v1.transferencia.facade;

import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.*;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaClientException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaNaoPermitidaException;
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
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaProducer;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public TransferenciaResponseDTO enviarTransferencia(TransferenciaRequestDTO request) throws JsonProcessingException {
        var transferenciaDTO = transferenciaService.salvarTransferencia(request);
        transferenciaProducer.enviarTransferencia(transferenciaDTO);
        return TransferenciaResponseDTO.builder().idTransferencia(transferenciaDTO.getIdTransferencia()).build();
    }

    public void consumirTransferencia(TransferenciaDTO transferenciaDTO) throws TransferenciaException {
        var cliente = buscarCliente(transferenciaDTO);

        var lock = new ReentrantLock();
        lock.lock();
        try {
            var contaOrigem = buscarConta(transferenciaDTO);
            validarConta(transferenciaDTO, contaOrigem);
            enviarNotificacao(transferenciaDTO);
            atualizarSaldo(transferenciaDTO, cliente);

            transferenciaService.atualizarStatusTransferencia(transferenciaDTO.getIdTransferencia(), StatusTransferenciaEnum.PROCESSADA);
        } finally {
            lock.unlock();
        }
    }

    private void validarConta(TransferenciaDTO transferenciaDTO, ContaResponseDTO contaOrigem) throws TransferenciaException {
        try {
            verificarContaAtiva(contaOrigem);
            verificarSaldoDisponivel(contaOrigem, transferenciaDTO.getValor());
            verificarLimiteDiario(contaOrigem, transferenciaDTO.getValor());
        } catch (ContaException ex) {
            log.error("Transferencia não permitida: {}",ex.getMessage());
            transferenciaService.salvarTransferenciaComErro(transferenciaDTO, StatusTransferenciaEnum.TRANSFERENCIA_NAO_PERMITIDA, ex.getMessage());
            throw new TransferenciaNaoPermitidaException(ex);
        }
    }

    private ContaResponseDTO buscarConta(TransferenciaDTO transferenciaDTO) throws TransferenciaException {
        try {
            return contaService.buscarConta(transferenciaDTO.getConta().getIdOrigem());
        } catch (ContaException ex) {
            log.error("Erro ao buscar conta: {}", ex.getMessage());
            transferenciaService.salvarTransferenciaComErro(transferenciaDTO, StatusTransferenciaEnum.ERRO_BUSCA_CONTA, ex.getMessage());
            throw new TransferenciaClientException(ex);
        }
    }

    private ClienteResponseDTO buscarCliente(TransferenciaDTO transferenciaDTO) throws TransferenciaException {
        try {
            return clienteService.buscarCliente(transferenciaDTO.getIdCliente());
        } catch (ClienteException ex) {
            log.error("Erro ao buscar cliente: {}", ex.getMessage());
            transferenciaService.salvarTransferenciaComErro(transferenciaDTO, StatusTransferenciaEnum.ERRO_BUSCA_CLIENTE, ex.getMessage());
            throw new TransferenciaClientException(ex);
        }
    }

    private void enviarNotificacao(TransferenciaDTO transferenciaDTO) throws TransferenciaException {
        try {
            var notificacao = prepararNotificacao(transferenciaDTO);
            notificacaoService.enviarNotificacao(notificacao);
        } catch (NotificacaoException ex) {
            log.error("Erro ao enviar notificacao: {}", ex.getMessage());
            transferenciaService.salvarTransferenciaComErro(transferenciaDTO, StatusTransferenciaEnum.ERRO_ENVIO_NOTIFICACAO, ex.getMessage());
            throw new TransferenciaClientException(ex);
        }
    }

    private void atualizarSaldo(TransferenciaDTO transferenciaDTO, ClienteResponseDTO cliente) throws TransferenciaException {
        try {
            var saldoRequest = prepararSaldo(transferenciaDTO, cliente);
            contaService.atualizarSaldo(saldoRequest);
        } catch (ContaException ex) {
            log.error("Erro ao atualizar saldo: {}", ex.getMessage());
            transferenciaService.salvarTransferenciaComErro(transferenciaDTO, StatusTransferenciaEnum.ERRO_ATUALIZA_SALDO, ex.getMessage());
            throw new TransferenciaClientException(ex);
        }
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
