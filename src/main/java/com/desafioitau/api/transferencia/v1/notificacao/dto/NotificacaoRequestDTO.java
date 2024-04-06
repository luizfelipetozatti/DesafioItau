package com.desafioitau.api.transferencia.v1.notificacao.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class NotificacaoRequestDTO {

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
