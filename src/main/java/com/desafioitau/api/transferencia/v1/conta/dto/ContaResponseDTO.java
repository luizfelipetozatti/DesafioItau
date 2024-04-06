package com.desafioitau.api.transferencia.v1.conta.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class ContaResponseDTO {

    private String id;
    private double saldo;
    private double limiteDiario;
    private boolean ativo;
}
