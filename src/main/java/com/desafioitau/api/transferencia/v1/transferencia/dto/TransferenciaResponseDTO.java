package com.desafioitau.api.transferencia.v1.transferencia.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class TransferenciaResponseDTO {

    private UUID idTransferencia;
}
