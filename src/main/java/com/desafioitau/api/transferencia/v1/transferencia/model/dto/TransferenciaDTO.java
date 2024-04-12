package com.desafioitau.api.transferencia.v1.transferencia.model.dto;

import com.desafioitau.api.transferencia.v1.transferencia.model.StatusTransferenciaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class TransferenciaDTO {

    private String idTransferencia;
    private String idCliente;
    private double valor;
    private Conta conta;
    private StatusTransferenciaEnum statusTransferenciaEnum;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder(toBuilder = true)
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}


