package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.mapper.TransferenciaMapper;
import com.desafioitau.api.transferencia.v1.transferencia.repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository repository;

    public String salvarTransaferencia(TransferenciaRequestDTO request) {
        return repository.save(TransferenciaMapper.INSTANCE.toModel(request)).getIdTransferencia();
    }
}
