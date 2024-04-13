package com.desafioitau.api.transferencia.v1.notificacao.service;

import com.desafioitau.api.transferencia.clients.NotificacaoClient;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.*;
import com.desafioitau.api.transferencia.v1.notificacao.dto.NotificacaoRequestDTO;
import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificacaoService {
    public static final String DEFAULT_CIRCUIT_BREAKER = "defaultCircuitBreaker";

    @Autowired
    private NotificacaoClient notificacaoClient;

    @CircuitBreaker(name = DEFAULT_CIRCUIT_BREAKER)
    public void enviarNotificacao(NotificacaoRequestDTO notificacao) throws NotificacaoException {
        log.info("Enviar notificacao: {}", notificacao);
        try {
            notificacaoClient.enviarNotificacao(notificacao);
        } catch (FeignException ex) {
            var mensagem = String.format("Erro ao enviar notificacao %s: %s", notificacao, ex.getMessage());
            log.error(mensagem);
            if (ex instanceof RetryableException) {
                throw new NotificacaoTentativasExcedidasException(ex.getCause());
            } else if (ex instanceof FeignException.InternalServerError) {
                throw new NotificacaoInternalServerErrorException(ex);
            } else if (ex instanceof FeignException.ServiceUnavailable) {
                throw new NotificacaoServiceUnavailableException(ex);
            } else {
                throw new NotificacaoClientException(ex);
            }
        }
    }
}
