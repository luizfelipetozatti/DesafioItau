package com.desafioitau.api.transferencia.v1.transferencia.fixture;

import com.desafioitau.api.transferencia.v1.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.Transferencia;

public class TransferenciaFixture {

    private static final String ID_CLIENTE = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f";
    private static final String ID_ORIGEM = "d0d32142-74b7-4aca-9c68-838aeacef96b";
    private static final String ID_DESTINO = "41313d7b-bd75-4c75-9dea-1f4be434007f";
    private static final double SALDO = 1000.00;

    public static TransferenciaRequestDTO getTransferenciaRequestDTO() {
        return TransferenciaRequestDTO.builder()
                .idCliente(ID_CLIENTE)
                .valor(SALDO)
                .conta(TransferenciaRequestDTO.Conta.builder()
                        .idOrigem(ID_ORIGEM)
                        .idDestino(ID_DESTINO)
                        .build())
                .build();
    }

    public static Transferencia getTransferencia() {
        return Transferencia.builder()
                .idTransferencia("1")
                .idCliente(ID_CLIENTE)
                .valor(SALDO)
                .conta(Transferencia.Conta.builder()
                        .idOrigem(ID_ORIGEM)
                        .idDestino(ID_DESTINO)
                        .build())
                .build();
    }
}
