package com.desafioitau.api.transferencia.configurations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class SpringDocConfigurationTest {

    private static final String HOST = "host";

    @InjectMocks
    private SpringDocConfiguration configuration;

    @Test
    void publicApiV1() {

        var result = configuration.publicApiV1();

        assertEquals("v1", result.getGroup());
        assertEquals(Collections.singletonList("/v1/**"), result.getPathsToMatch());
    }

    @Test
    void customOpenAPI() {

        var result = configuration.customOpenAPI(HOST);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getInfo());
    }
}
