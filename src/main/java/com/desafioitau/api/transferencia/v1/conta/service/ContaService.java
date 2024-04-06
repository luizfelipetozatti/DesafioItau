package com.desafioitau.api.transferencia.v1.conta.service;

import com.desafioitau.api.transferencia.clients.ContaClient;
import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.v1.conta.exception.ContaException;
import com.desafioitau.api.transferencia.v1.conta.exception.ContaInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.conta.exception.ContaNotFoundException;
import com.desafioitau.api.transferencia.v1.saldo.dto.SaldoRequestDTO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    @Autowired
    private ContaClient contaClient;

    public ContaResponseDTO buscarConta(String idOrigem) throws ContaException {
        try {
            return contaClient.buscarConta(idOrigem);
        } catch (FeignException.NotFound ex) {
            throw new ContaNotFoundException(ex.getCause());
        } catch (FeignException.InternalServerError ex) {
            throw new ContaInternalServerErrorException(ex);
        }
    }

    public ContaResponseDTO atualizarSaldo(SaldoRequestDTO request) throws ContaException {
        try {
            return contaClient.atualizarSaldo(request);
        } catch (FeignException.InternalServerError ex) {
            throw new ContaInternalServerErrorException(ex);
        }
    }
}
