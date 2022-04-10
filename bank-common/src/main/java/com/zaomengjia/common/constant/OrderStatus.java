package com.zaomengjia.common.constant;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/11 00:27
 */
public enum OrderStatus {
    CREATING("CREATING"),
    NORMAL("NORMAL"),
    ERROR("ERROR");

    public final String raw;
    OrderStatus(String raw) {
        this.raw = raw;
    }
}
