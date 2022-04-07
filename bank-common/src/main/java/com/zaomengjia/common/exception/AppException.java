package com.zaomengjia.common.exception;

import com.zaomengjia.common.constant.ResultCode;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 10:25
 */
public class AppException extends RuntimeException{
    private final ResultCode resultCode;

    private final String message;

    public AppException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.message = resultCode.message;
    }

    public AppException(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

}
