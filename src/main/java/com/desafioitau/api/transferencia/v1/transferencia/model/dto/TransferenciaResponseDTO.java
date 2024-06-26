package com.desafioitau.api.transferencia.v1.transferencia.model.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class TransferenciaResponseDTO {

    private String idTransferencia;
}
