package com.zaomengjia.auth.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/4 12:15
 */
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private Mono<Void> response(ServerWebExchange exchange, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    @NotNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if(ex instanceof AuthenticationServiceException) {
            return response(exchange, "1111");
        }
        else {
            return response(exchange, "2222");
        }
    }
}
