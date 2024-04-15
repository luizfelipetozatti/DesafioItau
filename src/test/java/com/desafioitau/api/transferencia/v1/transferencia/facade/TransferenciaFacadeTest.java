package com.desafioitau.api.transferencia.v1.transferencia.facade;

import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteNotFoundException;
import com.desafioitau.api.transferencia.exceptions.cliente.exception.ClienteServiceUnavailableException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaClientException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaNotFoundException;
import com.desafioitau.api.transferencia.exceptions.conta.exception.ContaServiceUnavailableException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoServiceUnavailableException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoTentativasExcedidasException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaClientException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaException;
import com.desafioitau.api.transferencia.exceptions.transferencia.TransferenciaNaoPermitidaException;
import com.desafioitau.api.transferencia.v1.cliente.fixture.ClienteFixture;
import com.desafioitau.api.transferencia.v1.cliente.service.ClienteService;
import com.desafioitau.api.transferencia.v1.conta.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.v1.conta.fixture.ContaFixture;
import com.desafioitau.api.transferencia.v1.conta.service.ContaService;
import com.desafioitau.api.transferencia.v1.notificacao.service.NotificacaoService;
import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaProducer;
import com.desafioitau.api.transferencia.v1.transferencia.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransferenciaFacadeTest {

    @Mock
    private TransferenciaService transferenciaService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private ContaService contaService;
    @Mock
    private NotificacaoService notificacaoService;
    @Mock
    private TransferenciaProducer transferenciaProducer;
    @InjectMocks
    private TransferenciaFacade facade;

    @BeforeEach
    void reset() {
        Mockito.reset(transferenciaService, contaService, clienteService, notificacaoService);
    }

    @Test
    void enviarTransferenciaDeveDarSucesso() throws JsonProcessingException {
        given(transferenciaService.salvarTransferencia(any())).willReturn(TransferenciaFixture.getTransferenciaDTO());
        assertDoesNotThrow(() -> facade.enviarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));
        verify(transferenciaProducer, times(1)).enviarTransferencia(any());
    }

    @Test
    void consumirTransferenciaDeveDarSucesso() throws Exception {
        given(clienteService.buscarCliente(anyString())).willReturn(ClienteFixture.getClienteResponseDTO());
        given(contaService.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());

        assertDoesNotThrow(() -> facade.consumirTransferencia(TransferenciaFixture.getTransferenciaDTO()));

        verify(contaService, times(1)).atualizarSaldo(any());
        verify(transferenciaService, times(1)).atualizarStatusTransferencia(anyString(), any());
        verify(transferenciaService, times(0)).salvarTransferenciaComErro(any(), any(), any());
        verify(notificacaoService, times(1)).enviarNotificacao(any());
    }

    @Test
    void consumirTransferenciaDeveDarExceptionQuandoAtualizarSaldo() throws Exception {
        given(clienteService.buscarCliente(anyString())).willReturn(ClienteFixture.getClienteResponseDTO());
        given(contaService.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());
        given(contaService.atualizarSaldo(any())).willThrow(ContaClientException.class);

        assertThrows(TransferenciaClientException.class, () -> facade.consumirTransferencia(TransferenciaFixture.getTransferenciaDTO()));

        verify(contaService, times(1)).atualizarSaldo(any());
        verify(transferenciaService, times(0)).atualizarStatusTransferencia(anyString(), any());
        verify(transferenciaService, times(1)).salvarTransferenciaComErro(any(), any(), any());
        verify(notificacaoService, times(1)).enviarNotificacao(any());
    }

    static Stream<Pair<Class<? extends TransferenciaClientException>, Class<? extends Exception>>> sourceConsumirTransferenciaQuandoBuscarCliente () {
        return Stream.of(
                Pair.of(TransferenciaClientException.class, ClienteNotFoundException.class),
                Pair.of(TransferenciaClientException.class, ClienteInternalServerErrorException.class),
                Pair.of(TransferenciaClientException.class, ClienteServiceUnavailableException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceConsumirTransferenciaQuandoBuscarCliente")
    void consumirTransferenciaDeveDarExceptionQuandoBuscarCliente(Pair<Class<? extends TransferenciaClientException>, Class<? extends Exception>> source) throws Exception {
        given(clienteService.buscarCliente(anyString())).willThrow(source.getRight());

        Assertions.assertThrows(source.getLeft(), () -> facade.consumirTransferencia(TransferenciaFixture.getTransferenciaDTO()));

        verify(contaService, times(0)).atualizarSaldo(any());
        verify(transferenciaService, times(0)).atualizarStatusTransferencia(anyString(), any());
        verify(transferenciaService, times(1)).salvarTransferenciaComErro(any(), any(), any());
        verify(notificacaoService, times(0)).enviarNotificacao(any());
    }

    static Stream<Pair<Class<? extends TransferenciaClientException>, Class<? extends Exception>>> sourceConsumirTransferenciaQuandoBuscarConta () {
        return Stream.of(
                Pair.of(TransferenciaClientException.class, ContaNotFoundException.class),
                Pair.of(TransferenciaClientException.class, ContaInternalServerErrorException.class),
                Pair.of(TransferenciaClientException.class, ContaServiceUnavailableException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceConsumirTransferenciaQuandoBuscarConta")
    void consumirTransferenciaDeveDarExceptionQuandoBuscarConta(Pair<Class<? extends TransferenciaClientException>, Class<? extends Exception>> source) throws Exception {
        given(contaService.buscarConta(anyString())).willThrow(source.getRight());

        Assertions.assertThrows(source.getLeft(), () -> facade.consumirTransferencia(TransferenciaFixture.getTransferenciaDTO()));

        verify(contaService, times(0)).atualizarSaldo(any());
        verify(transferenciaService, times(0)).atualizarStatusTransferencia(anyString(), any());
        verify(transferenciaService, times(1)).salvarTransferenciaComErro(any(), any(), any());
        verify(notificacaoService, times(0)).enviarNotificacao(any());
    }

    static Stream<Pair<Class<? extends Exception>, ContaResponseDTO>> sourceConsumirTransferenciaDeveDarException () {
        return Stream.of(
                Pair.of(TransferenciaNaoPermitidaException.class, ContaFixture.getContaResponseDTOInativa()),
                Pair.of(TransferenciaNaoPermitidaException.class, ContaFixture.getContaResponseDTOSaldoIndisponivel()),
                Pair.of(TransferenciaNaoPermitidaException.class, ContaFixture.getContaResponseDTOLimiteDiarioInsuficiente()),
                Pair.of(TransferenciaNaoPermitidaException.class, ContaFixture.getContaResponseDTOLimiteDiarioZerado())
        );
    }

    @ParameterizedTest
    @MethodSource("sourceConsumirTransferenciaDeveDarException")
    void consumirTransferenciaDeveDarException(Pair<Class<? extends Exception>, ContaResponseDTO> source) throws Exception {
        given(contaService.buscarConta(anyString())).willReturn(source.getRight());

        Assertions.assertThrows(source.getLeft(), () -> facade.consumirTransferencia(TransferenciaFixture.getTransferenciaDTO()));

        verify(contaService, times(0)).atualizarSaldo(any());
        verify(transferenciaService, times(0)).atualizarStatusTransferencia(anyString(), any());
        verify(transferenciaService, times(1)).salvarTransferenciaComErro(any(), any(), any());
        verify(notificacaoService, times(0)).enviarNotificacao(any());
    }

    static Stream<Pair<Class<? extends TransferenciaException>, Class<? extends NotificacaoException>>> sourceConsumirTransferenciaQuandoEnviarNotificacao() {
        return Stream.of(
                Pair.of(TransferenciaClientException.class, NotificacaoTentativasExcedidasException.class),
                Pair.of(TransferenciaClientException.class,NotificacaoInternalServerErrorException.class),
                Pair.of(TransferenciaClientException.class, NotificacaoServiceUnavailableException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceConsumirTransferenciaQuandoEnviarNotificacao")
    void consumirTransferenciaDeveDarExceptionQuantoEnviarNotificacao(Pair<Class<? extends TransferenciaException>, Class<? extends NotificacaoException>> source) throws Exception {
        willThrow(source.getRight()).given(notificacaoService).enviarNotificacao(any());
        given(clienteService.buscarCliente(anyString())).willReturn(ClienteFixture.getClienteResponseDTO());
        given(contaService.buscarConta(anyString())).willReturn(ContaFixture.getContaResponseDTO());

        Assertions.assertThrows(source.getLeft(), () -> facade.consumirTransferencia(TransferenciaFixture.getTransferenciaDTO()));

        verify(contaService, times(0)).atualizarSaldo(any());
        verify(transferenciaService, times(0)).atualizarStatusTransferencia(anyString(), any());
        verify(transferenciaService, times(1)).salvarTransferenciaComErro(any(), any(), any());
        verify(notificacaoService, times(1)).enviarNotificacao(any());
    }

}
