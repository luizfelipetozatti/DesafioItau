package com.desafioitau.api.transferencia.clients;

import com.desafioitau.api.transferencia.v1.cliente.dto.ClienteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${service.clientes.service}")
public interface ClienteClient {

    @GetMapping(value = "${service.clientes.dados-endpoint}")
    ClienteResponseDTO buscarCliente(@PathVariable(name = "idCliente") String idCliente);
}
