package com.zaomengjia.auth.handler;

import com.alibaba.fastjson.JSON;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/4 12:15
 */
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {



    private Mono<Void> writeResponse(ServerWebExchange exchange, Object o) {
        String s = JSON.toJSONString(o);
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().getHeaders().set("Content-Type", "text/json;charset=utf-8");
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }


    @NotNull
    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        if(ex instanceof AppException) {
            return writeResponse(exchange, ResultUtils.error(((AppException) ex).getResultCode()));
        }
        else if(ex instanceof ResponseStatusException) {
            ResponseStatusException ex1 = (ResponseStatusException) ex;
            switch (ex1.getRawStatusCode()) {
                case 404:
                    return writeResponse(exchange, ResultUtils.error(ResultCode.NOT_FOUND));
                case 503:
                    return writeResponse(exchange, ResultUtils.error(ResultCode.SERVER_UNAVAILABLE));
                default:
                    return writeResponse(exchange, ResultUtils.internalServerError(ex.getLocalizedMessage()));
            }
        }
        else {
            return writeResponse(exchange, ResultUtils.internalServerError(ex.getLocalizedMessage()));
        }
    }
}
