package com.zaomengjia.gateway.exception;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 15:02
 */
public class TokenErrorException extends AppException {
    public TokenErrorException() {
        super(ResultCode.TOKEN_ERROR);
    }
}
