package com.zaomengjia.common.service;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.utils.RedisUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Optional;

import static com.zaomengjia.common.constant.RedisKey.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 01:11
 */
@Service
public class OrderSimpleService {
    private final OrderMapper orderMapper;

    private final RedisUtils redisUtils;

    public OrderSimpleService(OrderMapper orderMapper, RedisUtils redisUtils) {
        this.orderMapper = orderMapper;
        this.redisUtils = redisUtils;
    }

    public int getUserOrderQuantity(String userId, String financialProductId, String seckillActivityId) {
        String key = MessageFormat.format("order-user-id-financial-product-id-seckill-activity-id-map::{0}::{1}::{2}", userId, financialProductId, seckillActivityId);
        Integer quantity = (Integer) redisUtils.get(key);
        return Optional.ofNullable(quantity).orElse(0);
    }

    public void decrUserOrderQuantity(String userId, String financialProductId, String seckillActivityId, long delta) {
        String key = MessageFormat.format("order-user-id-financial-product-id-seckill-activity-id-map::{0}::{1}::{2}", userId, financialProductId, seckillActivityId);
        long result = redisUtils.decr(key, delta);
        if(result < 0) {
            redisUtils.set(key, 0);
        }
    }

    private String getKeyById(String id) {
        return (String) redisUtils.hget(orderIdKeyMapKey(), id);
    }

    private String getKey(Order order) {
        return orderFullKey(order.getId(), order.getFinancialProductId(), order.getSeckillActivityId(), order.getUserId());
    }

    private void setKey(Order order) {
        String key = getKey(order);
        redisUtils.hset(orderIdKeyMapKey(), order.getId(), key);
    }

    public void setCache(Order order) {
        String key = getKey(order);
        redisUtils.set(key, order, 24 * 60 * 60);
        setKey(order);
    }

    public void setCache(Order order, long second) {
        String key = getKey(order);
        redisUtils.set(key, order, second);
        setKey(order);
    }

    public Order getCacheById(String id) {
        String key = getKeyById(id);
        if(key == null) {
            return null;
        }

        return ((JSONObject) redisUtils.get(key)).toJavaObject(Order.class);
    }

    public void deleteCache(String orderId) {
        String key = getKeyById(orderId);
        if(key == null) {
            return;
        }
        redisUtils.hdel(orderIdKeyMapKey(), getOrderFullKeyComponents(key)[0]);
    }

    public boolean containCache(Order order) {
        return redisUtils.containKey(getKey(order));
    }
}
