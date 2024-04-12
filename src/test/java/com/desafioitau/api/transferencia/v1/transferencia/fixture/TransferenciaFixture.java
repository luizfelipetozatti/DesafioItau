package com.desafioitau.api.transferencia.v1.transferencia.fixture;

import com.desafioitau.api.transferencia.v1.transferencia.model.StatusTransferenciaEnum;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.v1.transferencia.model.Transferencia;

public class TransferenciaFixture {

    private static final String ID_CLIENTE = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f";
    private static final String ID_ORIGEM = "d0d32142-74b7-4aca-9c68-838aeacef96b";
    private static final String ID_DESTINO = "41313d7b-bd75-4c75-9dea-1f4be434007f";
    private static final String ID_TRANSFERENCIA = "36397c51-dfd3-4fa0-b1fa-a1397a2c05cb";
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

    public static TransferenciaDTO getTransferenciaDTO() {
        return TransferenciaDTO.builder()
                .idTransferencia(ID_TRANSFERENCIA)
                .idCliente(ID_CLIENTE)
                .valor(SALDO)
                .statusTransferenciaEnum(StatusTransferenciaEnum.PENDENTE)
                .conta(TransferenciaDTO.Conta.builder()
                        .idOrigem(ID_ORIGEM)
                        .idDestino(ID_DESTINO)
                        .build())
                .build();
    }

    public static Transferencia getTransferencia() {
        return Transferencia.builder()
                .idTransferencia(ID_TRANSFERENCIA)
                .idCliente(ID_CLIENTE)
                .valor(SALDO)
                .statusTransferenciaEnum(StatusTransferenciaEnum.PENDENTE)
                .conta(Transferencia.Conta.builder()
                        .idOrigem(ID_ORIGEM)
                        .idDestino(ID_DESTINO)
                        .build())
                .build();
    }
}
