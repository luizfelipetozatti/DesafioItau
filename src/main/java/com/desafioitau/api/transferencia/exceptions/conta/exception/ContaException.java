package com.desafioitau.api.transferencia.exceptions.conta.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ContaException extends Exception {
    protected ContaException(Throwable cause) { super(cause); }
}
