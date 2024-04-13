package com.desafioitau.api.transferencia.exceptions.thread.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("{} - {} -> {} ({}) - {}",
                "FALHA_ASYNC",
                method.getClass().getName(),
                method.getName(),
                params,
                ex);
    }
}
