package com.desafioitau.api.transferencia.v1.notificacao.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class NotificacaoException extends Exception {
    protected NotificacaoException(Throwable cause) { super(cause); }
}
