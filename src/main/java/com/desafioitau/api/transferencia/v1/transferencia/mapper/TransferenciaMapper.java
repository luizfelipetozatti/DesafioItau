package com.desafioitau.api.transferencia.v1.transferencia.mapper;

import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.Transferencia;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransferenciaMapper {

    TransferenciaMapper INSTANCE = Mappers.getMapper(TransferenciaMapper.class);

    Transferencia toModel(TransferenciaRequestDTO request);
}
