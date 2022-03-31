package com.zaomengjia.common.utils;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.vo.ResultVO;
import lombok.Data;

/**
 * 统一返回工具类
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 00:36
 */
public final class ResultUtils {

    /**
     * 成功
     * @return 统一返回
     */
    public static ResultVO<?> success() {
        return new ResultVO<>(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message);
    }

    /**
     * 成功
     * @param data 数据
     * @return 统一返回
     */
    public static <T> ResultVO<?> success(T data) {
        return new ResultVO<>(ResultCode.SUCCESS.code, data);
    }

    /**
     * 错误统一返回
     * @param code 错误代号
     * @return 统一返回
     */
    public static ResultVO<?> error(ResultCode code) {
        return new ResultVO<>(code.code, code.message);
    }

    /**
     * 错误统一返回
     * @deprecated 请使用error(ResultCode code)，创建新的代码来代替消息。如果是从异常中获取message，请使用handler拦截，在logger中输出错误，用统一的代号（INTERNAL_SERVER_ERROR）返回。@orangeboy
     * @param code 错误代号
     * @param message 消息
     * @return 统一返回
     */
    @Deprecated
    public static ResultVO<?> error(ResultCode code, String message) {
        return new ResultVO<>(code.code, message);
    }
}
