package com.zaomengjia.order.handler;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:49
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    @ExceptionHandler(Exception.class)
    public ResultVO<?> internalServerError(Exception e) {
        logger.error("遇到了错误", e);
        return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

}
