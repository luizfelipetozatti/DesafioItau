package com.desafioitau.api.transferencia.v1.cliente.service;

import com.desafioitau.api.transferencia.clients.ClienteClient;
import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteNotFoundException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    public static final String DEFAULT_CIRCUIT_BREAKER = "defaultCircuitBreaker";

    @Autowired
    private ClienteClient clienteClient;
    //TODO cachear
    @CircuitBreaker(name = DEFAULT_CIRCUIT_BREAKER)
    public ClienteResponseDTO buscarCliente(String idCliente) throws ClienteException {
        try {
            return clienteClient.buscarCliente(idCliente);
        } catch (FeignException.NotFound ex) {
            throw new ClienteNotFoundException(ex.getCause());
        } catch (FeignException.InternalServerError ex) {
            throw new ClienteInternalServerErrorException(ex);
        } catch (FeignException.ServiceUnavailable ex) {
            throw new ClienteServiceUnavailableException(ex);
        }
    }
}
