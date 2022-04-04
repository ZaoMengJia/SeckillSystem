package com.zaomengjia.common.constant;

/**
 * 返回的code和message
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 00:38
 */
public enum ResultCode {
    SUCCESS(10000, "成功"),
    NOT_FOUND(20001, "迷路啦"),
    INTERNAL_SERVER_ERROR(20002, "服务器抛给你一个错误"),
    NO_SUCH_ACCOUNT_ERROR(30001, "没有这个用户"),
    NO_SUCH_PRODUCT_ERROR(30002,"没有这个理财产品"),
    NO_SUCH_ORDER_ERROR(30003,"没有这个订单"),
    NO_SUCH_DETAIL_ERROR(30004,"没有这个活动明细"),
    NO_SUCH_ACTIVITY_ERROR(30005,"没有这个秒杀活动")
    ;

    public final int code;
    public final String message;
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
