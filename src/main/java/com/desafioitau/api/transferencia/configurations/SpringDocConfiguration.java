package com.desafioitau.api.transferencia.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    private static final String MAIN_PACKAGE = "com.desafioitau.api.transferencia";

    @Bean
    public GroupedOpenApi publicApiV1() {
        return GroupedOpenApi.builder()
                .packagesToScan(MAIN_PACKAGE)
                .group("v1")
                .pathsToMatch("/v1/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.application.name}") String host) {
        return new OpenAPI()
                .addServersItem(new Server().url("/").description(host))
                .components(new Components())
                .info(new Info()
                        .title(host)
                        .description("Api responsável por realizar transferências entre contas")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
