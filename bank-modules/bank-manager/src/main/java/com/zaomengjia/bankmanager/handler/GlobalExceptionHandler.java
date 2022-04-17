package com.zaomengjia.bankmanager.handler;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 02:14
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultVO<?> notFound() {
        return ResultUtils.error(ResultCode.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultVO<?> patternError() {
        return ResultUtils.error(ResultCode.PATTERN_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResultVO<?> appException(AppException appException) {
        return ResultUtils.error(appException.getResultCode(), appException.getMessage());
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResultVO<?> internalServerError(HttpServerErrorException.InternalServerError e) {
        return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
