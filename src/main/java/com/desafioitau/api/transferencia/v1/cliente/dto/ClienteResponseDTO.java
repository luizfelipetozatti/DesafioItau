package com.desafioitau.api.transferencia.v1.cliente.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class ClienteResponseDTO {

    private String id;
    private String nome;
    private String telefone;
    private String tipoPessoa;
}
