package com.zaomengjia.auth.exception;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/4 13:33
 */
public class LoginErrorException extends AppException {
    public LoginErrorException(String message) {
        super(ResultCode.LOGIN_ERROR, message);
    }

    public LoginErrorException() {
        super(ResultCode.LOGIN_ERROR);
    }
}
