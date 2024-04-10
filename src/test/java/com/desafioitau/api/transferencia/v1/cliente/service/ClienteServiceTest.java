package com.desafioitau.api.transferencia.v1.cliente.service;

import com.desafioitau.api.transferencia.clients.ClienteClient;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.*;
import com.desafioitau.api.transferencia.v1.cliente.fixture.ClienteFixture;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteClient clienteClient;
    @InjectMocks
    private ClienteService clienteService;

    @Test
    void buscarClienteDeveDarSucesso() {
        given(clienteClient.buscarCliente(anyString())).willReturn(ClienteFixture.getClienteResponseDTO());

        Assertions.assertDoesNotThrow(() -> clienteService.buscarCliente(anyString()));
    }

    static Stream<Pair<Class<? extends FeignException>, Class<? extends ClienteException>>> sourceBuscarCliente(){
        return Stream.of(
                Pair.of(FeignException.NotFound.class, ClienteNotFoundException.class),
                Pair.of(FeignException.InternalServerError.class, ClienteInternalServerErrorException.class),
                Pair.of(FeignException.ServiceUnavailable.class, ClienteServiceUnavailableException.class),
                Pair.of(FeignException.class, ClienteInternalErrorException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceBuscarCliente")
    void buscarClienteDeveDarException(Pair<Class<? extends FeignException>, Class<? extends ClienteException>> source){
        given(clienteClient.buscarCliente(anyString())).willThrow(source.getLeft());
        assertThrows(source.getRight(), () -> clienteService.buscarCliente(anyString()));
    }

}
