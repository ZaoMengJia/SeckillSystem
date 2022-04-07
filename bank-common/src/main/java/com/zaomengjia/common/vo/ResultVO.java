package com.zaomengjia.common.vo;

import lombok.Data;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 00:59
 */
@Data
public class ResultVO<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultVO(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ResultVO(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
