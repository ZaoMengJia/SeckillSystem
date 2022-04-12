package com.zaomengjia.common.constant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 00:07
 */
public final class RedisKey {
    public static String saleProductDetailKey(String orderId, String financialId, String activityId) {
        return MessageFormat.format("{0}::{1}::{2}", Optional.ofNullable(orderId).orElse("-"), Optional.ofNullable(financialId).orElse("-"), Optional.of(activityId).orElse("-"));
    }

    public static String saleProductDetailActivityIdProductIdKeyMapKey() {
        return "sale-product-detail-activity-id-product-id-key-map";
    }

    public static String saleProductDetailIdKeyMapKey() {
        return "sale-product-detail-id-key-map";
    }

    public static String saleProductDetailMapKey() {
        return "sale-product-detail";
    }

    public static String orderIdKeyMapKey() {
        return "order-id-key-map";
    }

    public static String orderFullKey(String orderId, String financialProductId, String activityId, String userId) {
        return MessageFormat.format("order::{0}::{1}::{2}::{3}", orderId, financialProductId, activityId, userId);
    }

    public static String tokenBucketMapKey(String financialProductId, String seckillActivityId) {
        return MessageFormat.format("seckill-token-bucket::{0}::{1}", financialProductId, seckillActivityId);
    }

    public static String stockMapKey() {
        return "stock";
    }

    public static String stockDirtyKey() {
        return "stock::dirty";
    }

    public static String[] getOrderFullKeyComponents(String key) {
        return key.substring(7).split("::");
    }

    public static String[] getSaleProductDetailKeyComponents(String key) {
        return key.split("::");
    }
}
