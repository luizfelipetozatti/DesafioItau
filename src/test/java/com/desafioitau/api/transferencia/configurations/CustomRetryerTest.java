package com.desafioitau.api.transferencia.configurations;

import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomRetryerTest {

    @Mock
    private RetryableException retryableException;
    @InjectMocks
    private CustomRetryer customRetryer;

    @BeforeEach
    void reset() {
        Mockito.reset(retryableException);
    }

    @Test
    void continueOrPropagateDeveDarSucesso() {
        given(retryableException.status()).willReturn(429);
        assertDoesNotThrow(() -> customRetryer.continueOrPropagate(retryableException));
    }

    @Test
    void continueOrPropagateDeveDarExceptionQuandoStatus() {
        given(retryableException.status()).willReturn(500);
        assertThrows(RetryableException.class, () -> customRetryer.continueOrPropagate(retryableException));
    }

    @Test
    void continueOrPropagateDeveDarExceptionQuandoExcederTentativas() {
        int maxAttempts = 3;
        given(retryableException.status()).willReturn(429);

        for (int i = 0; i < maxAttempts; i++) {
            assertDoesNotThrow(() -> customRetryer.continueOrPropagate(retryableException));
        }

        assertThrows(RetryableException.class, () -> customRetryer.continueOrPropagate(retryableException));
    }

    @Test
    void cloneDeveDarSucesso() {
        CustomRetryer clonedRetryer = (CustomRetryer) customRetryer.clone();
        assertEquals(clonedRetryer, clonedRetryer);
    }
}
