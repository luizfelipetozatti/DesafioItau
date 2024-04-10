package com.desafioitau.api.transferencia.v1.conta.service;

import com.desafioitau.api.transferencia.clients.ContaClient;
import com.desafioitau.api.transferencia.exceptions.conta.exception.*;
import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.v1.conta.dto.SaldoRequestDTO;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContaService {
    public static final String DEFAULT_CIRCUIT_BREAKER = "defaultCircuitBreaker";

    @Autowired
    private ContaClient contaClient;

    @CircuitBreaker(name = DEFAULT_CIRCUIT_BREAKER)
    public ContaResponseDTO buscarConta(String idOrigem) throws ContaException {
        log.info("Buscar dados da conta com ID: {}", idOrigem);
        try {
            return contaClient.buscarConta(idOrigem);
        } catch (FeignException ex) {
            var mensagem = String.format("Erro ao buscar conta com ID %s: %s", idOrigem, ex.getMessage());
            log.error(mensagem);
            if (ex instanceof FeignException.NotFound) {
                throw new ContaNotFoundException(ex.getCause());
            } else if (ex instanceof FeignException.InternalServerError) {
                throw new ContaInternalServerErrorException(ex);
            } else if (ex instanceof FeignException.ServiceUnavailable) {
                throw new ContaServiceUnavailableException(ex);
            } else {
                throw new ContaInternalErrorException(ex);
            }
        }
    }

    public ContaResponseDTO atualizarSaldo(SaldoRequestDTO request) throws ContaException {
        log.info("Atualizar saldo com a request: {}", request);
        try {
            return contaClient.atualizarSaldo(request);
        } catch (FeignException ex) {
            var mensagem = String.format("Erro ao atualizar saldo com a request %s: %s", request, ex.getMessage());
            log.error(mensagem);
            if (ex instanceof FeignException.InternalServerError) {
                throw new ContaInternalServerErrorException(ex);
            } else {
                throw new ContaInternalErrorException(ex);
            }
        }
    }
}
