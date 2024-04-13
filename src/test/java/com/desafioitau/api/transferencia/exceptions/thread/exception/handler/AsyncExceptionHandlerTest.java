package com.desafioitau.api.transferencia.exceptions.thread.exception.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

@ExtendWith(MockitoExtension.class)
class AsyncExceptionHandlerTest {

    @InjectMocks
    AsyncExceptionHandler asyncExceptionHandler;

    @Test
    void handleUncaughtExceptionDeveDarSucesso() {
        Method method = ReflectionUtils.findMethod(String.class, "toString").get();
        Assertions.assertDoesNotThrow(() -> asyncExceptionHandler.handleUncaughtException(new Throwable(), method, ""));
    }
}
