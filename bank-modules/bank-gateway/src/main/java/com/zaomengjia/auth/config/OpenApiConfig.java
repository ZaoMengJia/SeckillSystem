package com.zaomengjia.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

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
                .group("auth")
                .packagesToScan("com.zaomengjia.*")
                .addOpenApiCustomiser(openApi -> {
                    openApi.path("/auth/weixin",
                            new PathItem()
                                    .post(
                                            new Operation()
                                                    .operationId("/auth/weixin")
                                                    .tags(Collections.singletonList("登录"))
                                                    .summary("微信登录")
                                                    .responses(new ApiResponses())
                                    )
                                    .addParametersItem(
                                            new Parameter()
                                                    .name("code")
                                                    .required(true)
                                                    .in("form")

                                    )
                    );

                    openApi.path("/auth/admin",
                            new PathItem()
                                    .post(
                                            new Operation()
                                                    .operationId("/auth/web")
                                                    .tags(Collections.singletonList("登录"))
                                                    .summary("管理员登录")
                                                    .responses(new ApiResponses())
                                    )
                                    .addParametersItem(
                                            new Parameter()
                                                    .name("username")
                                                    .required(true)
                                                    .in("form")

                                    )
                                    .addParametersItem(
                                            new Parameter()
                                                    .name("password")
                                                    .required(true)
                                                    .in("form")

                                    )
                    );

                })
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