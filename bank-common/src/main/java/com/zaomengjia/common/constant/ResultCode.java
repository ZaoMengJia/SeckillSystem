package com.zaomengjia.common.constant;

/**
 * 返回的code和message
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 00:38
 */
public enum ResultCode {
    SUCCESS(10000, "成功"),
    NOT_FOUND(20001, "迷路啦，您是想去华师吗"),
    INTERNAL_SERVER_ERROR(20002, "服务器抛给你一个错误"),
    PATTERN_ERROR(20003, "访问接口都不带全部参数都，想要干嘛？"),
    INVALID_REQUEST_ERROR(20004, "你想干嘛？"),
    INVALID_SIGNATURE(20005, "签名错啦大哥，好好回去看代码吧"),

    /**
     * 在黑名单里，30分钟解禁
     */
    HAVE_A_REST_PLEASE(20006, "大哥，喝瓶可乐冷静一下，那么急干嘛"),
    SERVER_UNAVAILABLE(50003,"服务器快要忙死啦奥利给"),


    NO_SUCH_ACCOUNT_ERROR(30001, "没有这个用户"),
    NO_SUCH_PRODUCT_ERROR(30002,"没有这个理财产品"),
    NO_SUCH_ORDER_ERROR(30003,"没有这个订单"),
    NO_SUCH_DETAIL_ERROR(30004,"没有这个活动明细"),
    NO_SUCH_ACTIVITY_ERROR(30005,"没有这个秒杀活动"),
    NO_SUCH_USER(30006, "是他！还没有注册就来访问接口！"),
    CANNOT_DELETE_SELF(30077, "不能删除自己哦"),
    SELL_OUT(30010, "已售罄"),
    CREATE_ORDER_ERROR(30011, "创建订单失败"),
    ORDER_EXCEED_LIMIT(30012, "每人限购哦～不能贪多哦～"),
    ACTIVITY_NOT_STARTED(30013, "活动未开始"),
    SECKILL_RETRY(30014, "交易失败"),
    SALE_PRODUCT_EXISTED(30015, "商品已存在"),

    LOGIN_ERROR(40001, "用户名或密码错误"),
    TOKEN_EXPIRED(40002, "登录信息超时，请重新登录"),
    TOKEN_ERROR(40003, "请重新登录"),
    ;

    public final int code;
    public final String message;
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
