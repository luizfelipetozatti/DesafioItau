package com.desafioitau.api.transferencia.v1.cliente.service;

import com.desafioitau.api.transferencia.clients.ClienteClient;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.*;
import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClienteService {
    public static final String DEFAULT_CIRCUIT_BREAKER = "defaultCircuitBreaker";

    @Autowired
    private ClienteClient clienteClient;
    //TODO cachear de forma que cada consulta armazene o usuário. Limite de 1000 registros com politica de substituição?
    @CircuitBreaker(name = DEFAULT_CIRCUIT_BREAKER)
    public ClienteResponseDTO buscarCliente(String idCliente) throws ClienteException {
        log.info("Buscar dados do cliente com ID: {}", idCliente);
        try {
            return clienteClient.buscarCliente(idCliente);
        } catch (FeignException ex) {
            var mensagem = String.format("Erro ao buscar cliente com ID %s: %s", idCliente, ex.getMessage());
            log.error(mensagem);
            if (ex instanceof FeignException.NotFound) {
                throw new ClienteNotFoundException(ex.getCause());
            } else if (ex instanceof FeignException.InternalServerError) {
                throw new ClienteInternalServerErrorException(ex);
            } else if (ex instanceof FeignException.ServiceUnavailable) {
                throw new ClienteServiceUnavailableException(ex);
            } else {
                throw new ClienteClientException(ex);
            }
        }
    }
}
