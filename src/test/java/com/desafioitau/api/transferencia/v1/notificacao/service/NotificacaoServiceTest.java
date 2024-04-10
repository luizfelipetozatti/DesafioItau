package com.desafioitau.api.transferencia.v1.notificacao.service;

import com.desafioitau.api.transferencia.clients.NotificacaoClient;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoInternalServerErrorException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoServiceUnavailableException;
import com.desafioitau.api.transferencia.exceptions.notificacao.exception.NotificacaoTentativasExcedidasException;
import feign.FeignException;
import feign.RetryableException;
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
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(SpringExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoClient notificacaoClient;
    @InjectMocks
    private NotificacaoService notificacaoService;

    @Test
    void enviarNotificacaoDeveDarSucesso(){
        Assertions.assertDoesNotThrow(() -> notificacaoService.enviarNotificacao(any()));
    }

    static Stream<Pair<Class<? extends Exception>, Class<? extends  Exception>>> sourceEnviarNotificacaoDeveDarException() {
        return Stream.of(
                Pair.of(RetryableException.class, NotificacaoTentativasExcedidasException.class),
                Pair.of(FeignException.InternalServerError.class, NotificacaoInternalServerErrorException.class),
                Pair.of(FeignException.ServiceUnavailable.class, NotificacaoServiceUnavailableException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceEnviarNotificacaoDeveDarException")
    void enviarNotificacaoDeveDarException(Pair<Class<? extends Exception>, Class<? extends  Exception>> source) {
        willThrow(source.getLeft()).given(notificacaoClient).enviarNotificacao(any());
        Assertions.assertThrows(source.getRight(), () -> notificacaoService.enviarNotificacao(any()));
    }

}
