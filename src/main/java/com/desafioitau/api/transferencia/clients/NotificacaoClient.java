package com.desafioitau.api.transferencia.clients;

import com.desafioitau.api.transferencia.configurations.FeignClientConfiguration;
import com.desafioitau.api.transferencia.v1.notificacao.dto.NotificacaoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao", url = "${service.bacen.service}", configuration = FeignClientConfiguration.class)
public interface NotificacaoClient {

    @PostMapping(value = "${service.bacen.notificacao-endpoint}")
    void enviarNotificacao(@RequestBody NotificacaoRequestDTO notificacao);
}
