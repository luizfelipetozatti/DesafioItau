package com.desafioitau.api.transferencia.exceptions.notificacao.exception.handler;

import com.desafioitau.api.transferencia.exceptions.ErrorInfo;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoServiceUnavailableException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoTentativasExcedidasException;
import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotificacaoExceptionHandler {

    private ErrorInfo traduzirException(String message) {
        return ErrorInfo.builder().mensagem(message).build();
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = {NotificacaoTentativasExcedidasException.class})
    @ResponseBody
    protected ErrorInfo handleTentativasExcedidasException(NotificacaoException ex) {
        return traduzirException(MessagesConstants.MSG_TENTATIVAS_EXCEDIDAS);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NotificacaoInternalServerErrorException.class})
    @ResponseBody
    protected ErrorInfo handleInternalServerErrorException(NotificacaoException ex) {
        return traduzirException(MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = {NotificacaoServiceUnavailableException.class})
    @ResponseBody
    protected ErrorInfo handleNotificacaoServiceUnavailableException(NotificacaoServiceUnavailableException ex) {
        return traduzirException(MessagesConstants.MSG_SERVICO_INDISPONIVEL);
    }
}
