package com.desafioitau.api.transferencia.v1.transferencia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Transferencia {

    @Id
    private String idTransferencia;
    private String idCliente;
    private double valor;
    private Conta conta;
    @Builder.Default
    private StatusTransferenciaEnum statusTransferenciaEnum = StatusTransferenciaEnum.PENDENTE;
    private String StatusTransferenciaDescricaoErro;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder(toBuilder = true)
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}


