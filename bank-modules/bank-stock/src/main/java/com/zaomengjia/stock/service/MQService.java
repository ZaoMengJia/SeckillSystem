package com.zaomengjia.stock.service;

import com.zaomengjia.common.constant.RabbitMQConstant;
import com.zaomengjia.common.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 01:28
 */
public interface MQService {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitMQConstant.ORDER_QUEUE_NAME),
            exchange = @Exchange(RabbitMQConstant.ORDER_QUEUE_NAME),
            key = {RabbitMQConstant.CREATE_ORDER_ROUTING_NAME}
    ))
    @RabbitHandler
    void onReceivedCreateOrderMessage(Order order);
}
