package com.desafioitau.api.transferencia.clients;

import com.desafioitau.api.transferencia.configurations.FeignClientConfiguration;
import com.desafioitau.api.transferencia.v1.notificacao.dto.NotificacaoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${external-api.notificacoes.name}", url = "${external-api.notificacoes.url}", configuration = FeignClientConfiguration.class)
public interface NotificacaoClient {

    @PostMapping(value = "${external-api.notificacoes.notificacao-endpoint}")
    void enviarNotificacao(@RequestBody NotificacaoRequestDTO notificacao);
}
