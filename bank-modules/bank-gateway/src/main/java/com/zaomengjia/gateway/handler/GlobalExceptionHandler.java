package com.zaomengjia.gateway.handler;

import com.zaomengjia.gateway.utils.ResponseWriter;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/4 12:15
 */
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NotNull
    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        if(ex instanceof AppException) {
            logger.debug("遇到了应用层面错误", ex);
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
        else if (ex instanceof AuthenticationCredentialsNotFoundException) {
            return ResponseWriter.writeResponse(exchange, ResultUtils.error(ResultCode.TOKEN_ERROR));
        }
        else {
            logger.error("拦截到了错误: {}", ex.getLocalizedMessage());
            ex.printStackTrace();
            return ResponseWriter.writeResponse(exchange, ResultUtils.internalServerError(ex.getLocalizedMessage()));
        }
    }
}
