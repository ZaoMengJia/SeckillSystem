package com.zaomengjia.gateway.exception;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/4 13:32
 */
public class TokenExpiredException extends AppException {
    public TokenExpiredException() {
        super(ResultCode.TOKEN_EXPIRED);
    }
}
