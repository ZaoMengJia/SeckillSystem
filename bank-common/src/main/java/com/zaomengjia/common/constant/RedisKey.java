package com.zaomengjia.common.constant;

import java.text.MessageFormat;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 00:07
 */
public final class RedisKey {

    public static String saleProductDetailActivityIdProductIdKeyMapKey() {
        return "sale-product-detail-activity-id-product-id-key-map";
    }

    public static String seckillActivityKey(String id) {
        return "seckill-activity::" + id;
    }

    public static String saleProductDetailIdKeyMapKey() {
        return "sale-product-detail-id-key-map";
    }

    public static String saleProductDetailKey(String id, String financialProductId, String seckillActivityId) {
        return MessageFormat.format("sale-product-detail::{0}::{1}::{2}", id, financialProductId, seckillActivityId);
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

    public static String stockMapKey(String financialProductId, String seckillActivityId) {
        return "stock-detail::" + financialProductId + "::" + seckillActivityId;
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
