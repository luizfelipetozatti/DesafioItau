package com.desafioitau.api.transferencia.exceptions.cliente.exception.handler;

import com.desafioitau.api.transferencia.exceptions.ErrorInfo;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteServiceUnavailableException;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteNotFoundException;
import com.desafioitau.api.transferencia.v1.constants.MessagesConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClienteExceptionHandler {

    private ErrorInfo traduzirException(String message) {
        return ErrorInfo.builder().mensagem(message).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ClienteNotFoundException.class})
    protected ErrorInfo handleClienteNotFoundException(ClienteNotFoundException ex) {
        return traduzirException(MessagesConstants.MSG_CLIENTE_NAO_ENCONTRADO);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {ClienteInternalServerErrorException.class})
    protected ErrorInfo handleClienteInternalServerErrorException(ClienteInternalServerErrorException ex) {
        return traduzirException(MessagesConstants.MSG_ERRO_INTERNO_DO_SERVIDOR);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = {ClienteServiceUnavailableException.class})
    protected ErrorInfo handleClientServiceUnavailableException(ClienteServiceUnavailableException ex) {
        return traduzirException(MessagesConstants.MSG_SERVICO_INDISPONIVEL);
    }
}
