package com.desafioitau.api.transferencia.exceptions.notificacao.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class NotificacaoException extends Exception {
    protected NotificacaoException(Throwable cause) { super(cause); }
}
