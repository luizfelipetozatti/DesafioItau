package com.desafioitau.api.transferencia.v1.cliente.service;

import com.desafioitau.api.transferencia.clients.ClienteClient;
import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteNotFoundException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteInternalServerErrorException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteClient clienteClient;
    //TODO cachear
    //TODO avaliar fallback ou retry circuit break
    public ClienteResponseDTO buscarCliente(String idCliente) throws ClienteException {
        try {
            return clienteClient.buscarCliente(idCliente);
        } catch (FeignException.NotFound ex) {
            throw new ClienteNotFoundException(ex.getCause());
        } catch (FeignException.InternalServerError ex) {
            throw new ClienteInternalServerErrorException(ex);
        }
    }
}
