package com.zaomengjia.gateway.config;

import com.zaomengjia.gateway.handler.GlobalExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 10:33
 */
@Configuration
public class GatewayConfig {

    @Bean
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
