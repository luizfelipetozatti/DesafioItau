package com.desafioitau.api.transferencia.clients;

import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.v1.conta.dto.SaldoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "contas", url = "${service.contas.service}")
public interface ContaClient {

    @GetMapping(value = "${service.contas.dados-endpoint}")
    ContaResponseDTO buscarConta(@PathVariable(name = "idConta") String idConta);

    @PutMapping(value = "${service.contas.saldo-endpoint}")
    ContaResponseDTO atualizarSaldo(@RequestBody SaldoRequestDTO request);
}
