package com.zaomengjia.product.handler;

import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:49
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResultVO<?> appException(AppException appException) {
        return ResultUtils.error(appException.getResultCode(), appException.getMessage());
    }
}
