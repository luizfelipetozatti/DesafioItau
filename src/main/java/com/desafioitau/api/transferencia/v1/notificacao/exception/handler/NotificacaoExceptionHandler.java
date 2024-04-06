package com.desafioitau.api.transferencia.v1.notificacao.exception.handler;

import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoTentativasExcedidasException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotificacaoExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = {NotificacaoTentativasExcedidasException.class})
    @ResponseBody
    protected String handleTentativasExcedidasException(NotificacaoException ex) {
        return MessagesConstants.MSG_TENTATIVAS_EXCEDIDAS;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NotificacaoInternalServerErrorException.class})
    @ResponseBody
    protected String handleInternalServerErrorException(NotificacaoException ex) {
        return MessagesConstants.MSG_SERVICO_INDISPONIVEL;
    }
}
