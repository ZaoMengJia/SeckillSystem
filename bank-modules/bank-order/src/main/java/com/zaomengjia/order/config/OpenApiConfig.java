package com.zaomengjia.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 11:28
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("order")
                .packagesToScan("com.zaomengjia.order.controller")
                .build();
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("造梦珈")
                        .contact(new Contact().name("ornageboy"))
                );
    }
}