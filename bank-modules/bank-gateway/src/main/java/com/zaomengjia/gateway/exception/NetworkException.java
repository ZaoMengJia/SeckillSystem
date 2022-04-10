package com.zaomengjia.gateway.exception;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 11:05
 */
public class NetworkException extends AppException {
    public NetworkException() {
        super(ResultCode.INTERNAL_SERVER_ERROR, "网络异常");
    }
}
