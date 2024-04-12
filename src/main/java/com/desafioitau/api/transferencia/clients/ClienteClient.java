package com.desafioitau.api.transferencia.clients;

import com.desafioitau.api.transferencia.configurations.FeignClientConfiguration;
import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${external-api.clientes.name}", url = "${external-api.clientes.url}", configuration = FeignClientConfiguration.class)
public interface ClienteClient {

    @GetMapping(value = "${external-api.clientes.dados-endpoint}")
    ClienteResponseDTO buscarCliente(@PathVariable(name = "idCliente") String idCliente);
}
