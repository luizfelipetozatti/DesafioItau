package com.desafioitau.api.transferencia.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorInfo {
    private String mensagem;
}
