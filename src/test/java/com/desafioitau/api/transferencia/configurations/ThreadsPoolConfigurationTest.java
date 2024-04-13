package com.desafioitau.api.transferencia.configurations;

import com.desafioitau.api.transferencia.exceptions.thread.exception.handler.AsyncExceptionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ThreadsPoolConfigurationTest {

    @Mock
    AsyncExceptionHandler asyncExceptionHandler;

    @InjectMocks
    ThreadsPoolConfiguration threadsPoolConfiguration;

    @Test
    void getAsyncExecutorDeveDarSucesso() {
        var executor = threadsPoolConfiguration.getAsyncExecutor();
        Assertions.assertNotEquals(executor, null);
    }

    @Test
    void getAsyncUncaughtExceptionHandler() {
        var asyncUncaught = threadsPoolConfiguration.getAsyncUncaughtExceptionHandler();
        Assertions.assertNotEquals(asyncUncaught, null);
    }
}
