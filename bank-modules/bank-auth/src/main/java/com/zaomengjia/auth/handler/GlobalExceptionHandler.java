package com.zaomengjia.auth.handler;

import com.alibaba.fastjson.JSON;
import com.zaomengjia.auth.utils.ResponseWriter;
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

    @NotNull
    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        if(ex instanceof AppException) {
            return ResponseWriter.writeResponse(exchange, ResultUtils.error(((AppException) ex).getResultCode()));
        }
        else if(ex instanceof ResponseStatusException) {
            ResponseStatusException ex1 = (ResponseStatusException) ex;
            switch (ex1.getRawStatusCode()) {
                case 404:
                    return ResponseWriter.writeResponse(exchange, ResultUtils.error(ResultCode.NOT_FOUND));
                case 503:
                    return ResponseWriter.writeResponse(exchange, ResultUtils.error(ResultCode.SERVER_UNAVAILABLE));
                default:
                    return ResponseWriter.writeResponse(exchange, ResultUtils.internalServerError(ex.getLocalizedMessage()));
            }
        }
        else {
            return ResponseWriter.writeResponse(exchange, ResultUtils.internalServerError(ex.getLocalizedMessage()));
        }
    }
}
