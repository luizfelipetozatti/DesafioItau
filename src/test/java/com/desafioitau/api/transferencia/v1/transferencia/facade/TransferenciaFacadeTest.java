package com.desafioitau.api.transferencia.v1.transferencia.facade;

import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteNotFoundException;
import com.desafioitau.api.transferencia.v1.cliente.exception.ClienteServiceUnavailableException;
import com.desafioitau.api.transferencia.v1.cliente.fixture.ClienteFixture;
import com.desafioitau.api.transferencia.v1.cliente.service.ClienteService;
import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.v1.conta.exception.*;
import com.desafioitau.api.transferencia.v1.conta.fixture.ContaFixture;
import com.desafioitau.api.transferencia.v1.conta.service.ContaService;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoInternalServerErrorException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoServiceUnavailableException;
import com.desafioitau.api.transferencia.v1.notificacao.exception.NotificacaoTentativasExcedidasException;
import com.desafioitau.api.transferencia.v1.notificacao.service.NotificacaoService;
import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class TransferenciaFacadeTest {

    @Mock
    TransferenciaService transferenciaService;
    @Mock
    ClienteService clienteService;
    @Mock
    ContaService contaService;
    @Mock
    NotificacaoService notificacaoService;
    @InjectMocks
    TransferenciaFacade facade;

    @BeforeEach
    void reset() {
        Mockito.reset(transferenciaService, contaService, clienteService, notificacaoService);
    }

    @Test
    void efetuarTransferenciaDeveDarSucesso() throws Exception {
        given(clienteService.buscarCliente(anyString())).willReturn(ClienteFixture.getClienteResponseDTO());
        given(contaService.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());

        assertDoesNotThrow(() -> facade.efetuarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));

        verify(transferenciaService, times(1)).salvarTransaferencia(any());
        verify(contaService, times(1)).atualizarSaldo(any());
    }

    static Stream<Class<? extends Exception>> sourceEfetuarTransferenciaQuandoBuscarCliente () {
        return Stream.of(ClienteNotFoundException.class,
                ClienteInternalServerErrorException.class,
                ClienteServiceUnavailableException.class
        );
    }

    @ParameterizedTest
    @MethodSource("sourceEfetuarTransferenciaQuandoBuscarCliente")
    void efetuarTransferenciaDeveDarExceptionQuandoBuscarCliente(Class<? extends Exception> source) throws Exception {
        given(clienteService.buscarCliente(anyString())).willThrow(source);

        Assertions.assertThrows(source, () -> facade.efetuarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));

        verify(transferenciaService, times(0)).salvarTransaferencia(any());
        verify(contaService, times(0)).atualizarSaldo(any());
    }

    static Stream<Class<? extends Exception>> sourceEfetuarTransferenciaQuandoBuscarConta () {
        return Stream.of(
                ContaNotFoundException.class,
                ContaInternalServerErrorException.class,
                ContaServiceUnavailableException.class
        );
    }

    @ParameterizedTest
    @MethodSource("sourceEfetuarTransferenciaQuandoBuscarConta")
    void efetuarTransferenciaDeveDarExceptionQuandoBuscarConta(Class<? extends Exception> source) throws Exception {
        given(contaService.buscarConta(anyString())).willThrow(source);

        Assertions.assertThrows(source, () -> facade.efetuarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));

        verify(transferenciaService, times(0)).salvarTransaferencia(any());
        verify(contaService, times(0)).atualizarSaldo(any());
    }

    static Stream<Pair<Class<? extends Exception>, ContaResponseDTO>> sourceEfetuarTransferenciaDeveDarException () {
        return Stream.of(
                Pair.of(ContaInativaException.class, ContaFixture.getContaResponseDTOInativa()),
                Pair.of(ContaSaldoIndisponivelException.class, ContaFixture.getContaResponseDTOSaldoIndisponivel()),
                Pair.of(ContaLimiteDiarioInsuficienteException.class, ContaFixture.getContaResponseDTOLimiteDiarioInsuficiente()),
                Pair.of(ContaLimiteDiarioZeradoException.class, ContaFixture.getContaResponseDTOLimiteDiarioZerado())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceEfetuarTransferenciaDeveDarException")
    void efetuarTransferenciaDeveDarException(Pair<Class<? extends Exception>, ContaResponseDTO> source) throws Exception {
        given(contaService.buscarConta(anyString())).willReturn(source.getRight());

        Assertions.assertThrows(source.getLeft(), () -> facade.efetuarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));

        verify(transferenciaService, times(0)).salvarTransaferencia(any());
        verify(contaService, times(0)).atualizarSaldo(any());
    }

    static Stream<Class<? extends NotificacaoException>> sourceEfetuarTransferenciaQuandoEnviarNotificacao() {
        return Stream.of(
                NotificacaoTentativasExcedidasException.class,
                NotificacaoInternalServerErrorException.class,
                NotificacaoServiceUnavailableException.class
        );
    }

    @ParameterizedTest
    @MethodSource("sourceEfetuarTransferenciaQuandoEnviarNotificacao")
    void efetuarTransferenciaDeveDarExceptionQuantoEnviarNotificacao(Class<? extends NotificacaoException> source) throws Exception {
        willThrow(source).given(notificacaoService).enviarNotificacao(any());
        given(clienteService.buscarCliente(anyString())).willReturn(ClienteFixture.getClienteResponseDTO());
        given(contaService.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());

        Assertions.assertThrows(source, () -> facade.efetuarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));

        verify(transferenciaService, times(1)).salvarTransaferencia(any());
        verify(contaService, times(0)).atualizarSaldo(any());
    }

}
