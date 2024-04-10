package com.desafioitau.api.transferencia.v1.conta.service;

import com.desafioitau.api.transferencia.clients.ContaClient;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaNotFoundException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaServiceUnavailableException;
import com.desafioitau.api.transferencia.v1.conta.fixture.ContaFixture;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.CLOSED;
import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContaServiceTest {

    @MockBean
    private ContaClient contaClient;
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    @Autowired
    private ContaService contaService;

    @BeforeEach
    public void reset() {
        Mockito.reset(contaClient);
        circuitBreakerRegistry.circuitBreaker(ContaService.DEFAULT_CIRCUIT_BREAKER).reset();
    }

    @Test
    void buscarContaDeveDarSucesso() {
        given(contaClient.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());
        Assertions.assertDoesNotThrow(() -> contaService.buscarConta(anyString()));
    }

    static Stream<Pair<Class<? extends FeignException>, Class<? extends Exception>>> sourceBuscarConta(){
        return Stream.of(Pair.of(FeignException.NotFound.class, ContaNotFoundException.class),
                Pair.of(FeignException.InternalServerError.class, ContaInternalServerErrorException.class),
                Pair.of(FeignException.ServiceUnavailable.class, ContaServiceUnavailableException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBuscarConta")
    void efetuarTransferenciaDeveDarExceptionQuandoBuscarConta(Pair<Class<? extends FeignException>, Class<? extends Exception>> source) throws Exception {
        given(contaService.buscarConta(anyString())).willThrow(source.getLeft());

        Assertions.assertThrows(source.getRight(), () -> contaService.buscarConta(anyString()));
    }

    @Test
    void atualizarSaldoDeveDarSucesso() {
        Assertions.assertDoesNotThrow(() -> contaService.atualizarSaldo(any()));
    }

    @Test
    void atualizarSaldoDeveDarException() {
        given(contaClient.atualizarSaldo(any())).willThrow(FeignException.InternalServerError.class);
        Assertions.assertThrows(ContaInternalServerErrorException.class, () -> contaService.atualizarSaldo(any()));
    }

    @Test
    void efetuarTransferenciaDeveDarCircuitBreakerOpenQuandoBuscarConta() throws Exception {
        given(contaService.buscarConta(anyString())).willThrow(FeignException.ServiceUnavailable.class);

        var circuitBreaker = circuitBreakerRegistry.circuitBreaker(ContaService.DEFAULT_CIRCUIT_BREAKER);
        assertEquals(CLOSED, circuitBreaker.getState());

        for (int i = 0; i < circuitBreaker.getCircuitBreakerConfig().getMinimumNumberOfCalls() - 1; i++) {
            Assertions.assertThrows(ContaServiceUnavailableException.class, () -> contaService.buscarConta("123"));
        }

        assertEquals(OPEN, circuitBreaker.getState());
        Assertions.assertThrows(CallNotPermittedException.class, () -> contaService.buscarConta("123"));
        assertEquals(circuitBreaker.getCircuitBreakerConfig().getMinimumNumberOfCalls() - 1, circuitBreaker.getMetrics().getNumberOfFailedCalls());
    }

}

