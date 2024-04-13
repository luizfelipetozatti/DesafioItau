package com.desafioitau.api.transferencia.configurations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FeignClientConfigurationTest {

    @InjectMocks
    private FeignClientConfiguration feignClientConfiguration;

    @Test
    void feignRetryerBeanShouldBeConfigured() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(FeignClientConfiguration.class);
        context.refresh();

        assertTrue(context.containsBean("feignRetryer"));
        assertNotNull(context.getBean("feignRetryer"));
        assertEquals(CustomRetryer.class, context.getBean("feignRetryer").getClass());

        context.close();
    }
}
