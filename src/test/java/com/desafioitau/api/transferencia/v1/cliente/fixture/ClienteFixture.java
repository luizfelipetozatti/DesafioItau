package com.desafioitau.api.transferencia.v1.cliente.fixture;

import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;

public class ClienteFixture {
    private static final String ID_CLIENTE = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f";
    private static final String NOME = "Luiz Felipe Tozatti";
    private static final String TIPO_PESSOA = "Fisica";
    private static final String TELEFONE = "(99)99999-9999";

    public static ClienteResponseDTO getClienteResponseDTO() {
        return ClienteResponseDTO.builder()
                .id(ID_CLIENTE)
                .nome(NOME)
                .tipoPessoa(TIPO_PESSOA)
                .telefone(TELEFONE)
                .build();
    }
}
