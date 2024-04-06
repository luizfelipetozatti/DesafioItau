package com.desafioitau.api.transferencia.v1.transferencia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class TransferenciaRequestDTO {

    private String idCliente;
    private double valor;
    private Conta conta;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder(toBuilder = true)
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}
