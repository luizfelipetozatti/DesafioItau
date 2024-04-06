package com.desafioitau.api.transferencia.v1.conta.service;

import com.desafioitau.api.transferencia.clients.ContaClient;
import com.desafioitau.api.transferencia.v1.conta.exception.ContaNotFoundException;
import com.desafioitau.api.transferencia.v1.conta.exception.ContaInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.conta.fixture.ContaFixture;
import feign.FeignException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class ContaServiceTest {

    @Mock
    private ContaClient contaClient;
    @InjectMocks
    private ContaService contaService;

    @Test
    void buscarContaDeveDarSucesso() {
        given(contaClient.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());
        Assertions.assertDoesNotThrow(() -> contaService.buscarConta(anyString()));
    }

    static Stream<Pair<Class<? extends FeignException>, Class<? extends Exception>>> sourceBuscarConta(){
        return Stream.of(Pair.of(FeignException.NotFound.class, ContaNotFoundException.class),
                Pair.of(FeignException.InternalServerError.class, ContaInternalServerErrorException.class));
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

}
