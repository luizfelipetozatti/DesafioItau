package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.mapper.TransferenciaMapper;
import com.desafioitau.api.transferencia.v1.transferencia.model.StatusTransferenciaEnum;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.repository.TransferenciaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository repository;

    public TransferenciaDTO salvarTransferencia(TransferenciaRequestDTO request) {
        log.info("Salva TransferenciaRequestDTO no banco.");
        var transferencia = repository.save(TransferenciaMapper.INSTANCE.toModel(request));
        return TransferenciaMapper.INSTANCE.toDTO(transferencia);
    }

    public void salvarTransferenciaComErro(TransferenciaDTO transferenciaDTO, StatusTransferenciaEnum statusTransferenciaEnum, String mensagem) {
        var transferenciaDTOComErro = transferenciaDTO.toBuilder()
                .statusTransferenciaEnum(statusTransferenciaEnum)
                .StatusTransferenciaDescricaoErro(mensagem)
                .build();
        log.info("Salva transferenciaDTO com erro no banco: {}", transferenciaDTOComErro);
        repository.save(TransferenciaMapper.INSTANCE.toModel(transferenciaDTOComErro));
    }

    public void atualizarStatusTransferencia(String transferenciaID, StatusTransferenciaEnum statusTransferenciaEnum) {
        log.info("Atualizar status transferencia no banco.");
        repository.findByIdAndUpdateStatusTransferencia(transferenciaID, statusTransferenciaEnum);
    }
}
