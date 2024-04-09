package com.desafioitau.api.transferencia.v1.notificacao.service;

import com.desafioitau.api.transferencia.clients.NotificacaoClient;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoServiceUnavailableException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoTentativasExcedidasException;
import com.desafioitau.api.transferencia.v1.notificacao.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoInternalServerErrorException;
import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoClient notificacaoClient;

    @CircuitBreaker(name = "defaultCircuitBreaker")
    public void enviarNotificacao(NotificacaoRequestDTO notificacao) throws NotificacaoException {
        try {
            notificacaoClient.enviarNotificacao(notificacao);
        } catch (RetryableException ex) {
            throw new NotificacaoTentativasExcedidasException(ex);
        } catch (FeignException.InternalServerError ex) {
            throw new NotificacaoInternalServerErrorException(ex);
        } catch (FeignException.ServiceUnavailable ex) {
            throw new NotificacaoServiceUnavailableException(ex);
        }
    }
}
