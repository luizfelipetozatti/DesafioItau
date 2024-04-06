package com.desafioitau.api.transferencia.v1.conta.fixture;

import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;

public class ContaFixture {

    private static final double SALDO = 1000.00;

    public static ContaResponseDTO getContaResponseDTO() {
        return ContaResponseDTO.builder()
                .ativo(true)
                .saldo(SALDO)
                .limiteDiario(SALDO)
                .build();
    }

    public static ContaResponseDTO getContaResponseDTOInativa() {
        return ContaResponseDTO.builder()
                .ativo(false)
                .build();
    }

    public static ContaResponseDTO getContaResponseDTOSaldoIndisponivel() {
        return ContaResponseDTO.builder()
                .ativo(true)
                .saldo(0)
                .build();
    }

    public static ContaResponseDTO getContaResponseDTOLimiteDiarioInsuficiente() {
        return ContaResponseDTO.builder()
                .ativo(true)
                .saldo(SALDO)
                .limiteDiario(1)
                .build();
    }

    public static ContaResponseDTO getContaResponseDTOLimiteDiarioZerado() {
        return ContaResponseDTO.builder()
                .ativo(true)
                .saldo(SALDO)
                .limiteDiario(0)
                .build();
    }
}
