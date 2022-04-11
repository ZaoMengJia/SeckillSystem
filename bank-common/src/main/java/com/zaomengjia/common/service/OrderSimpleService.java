package com.zaomengjia.common.service;

import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.utils.RedisUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.zaomengjia.common.constant.RedisKey.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 01:11
 */
@Service
@Transactional
public class OrderSimpleService {
    private final OrderMapper orderMapper;

    private final RedisUtils redisUtils;

    public OrderSimpleService(OrderMapper orderMapper, RedisUtils redisUtils) {
        this.orderMapper = orderMapper;
        this.redisUtils = redisUtils;
    }

    private String getKeyById(String id) {
        return (String) redisUtils.hget(orderIdKeyMapKey(), id);
    }

    private String getKey(Order order) {
        return orderFullKey(order.getSeckillActivityId(), order.getFinancialProductId(), order.getSeckillActivityId(), order.getUserId());
    }

    private void setKey(Order order) {
        String key = getKey(order);
        redisUtils.hset(orderIdKeyMapKey(), order.getId(), key);
    }

    public void setCache(Order order) {
        String key = getKey(order);
        redisUtils.set(key, order, 60 * 12 * 12);
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

        return (Order) redisUtils.get(key);
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
