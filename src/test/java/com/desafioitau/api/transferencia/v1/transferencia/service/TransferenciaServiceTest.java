package com.desafioitau.api.transferencia.v1.transferencia.service;

import com.desafioitau.api.transferencia.v1.transferencia.fixture.TransferenciaFixture;
import com.desafioitau.api.transferencia.v1.transferencia.model.StatusTransferenciaEnum;
import com.desafioitau.api.transferencia.v1.transferencia.repository.TransferenciaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository repository;
    @InjectMocks
    private TransferenciaService service;

    @BeforeEach
    void reset() {
        Mockito.reset(repository);
    }

    @Test
    void salvarTransaferenciaDeveDarSucesso() {
        given(repository.save(any())).willReturn(TransferenciaFixture.getTransferencia());
        Assertions.assertDoesNotThrow(() -> service.salvarTransferencia(TransferenciaFixture.getTransferenciaRequestDTO()));
    }

    @Test
    void atualizarStatusTransferenciaDeveDarSucesso() {
        Assertions.assertDoesNotThrow(() -> service.atualizarStatusTransferencia(anyString(), any()));
    }

    @Test
    void salvarTransferenciaComErroDeveDarSucesso() {
        Assertions.assertDoesNotThrow(() -> service.salvarTransferenciaComErro(TransferenciaFixture.getTransferenciaDTO(),
                StatusTransferenciaEnum.ERRO_ATUALIZA_SALDO, ""));
    }

}
