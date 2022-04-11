package com.zaomengjia.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.zaomengjia.common.constant.RabbitMQConstant;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.stock.service.MQService;
import com.zaomengjia.stock.service.StockService;
import com.zaomengjia.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 01:29
 */
@Service
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(RabbitMQConstant.ORDER_QUEUE_NAME),
        exchange = @Exchange(RabbitMQConstant.DEFAULT_EXCHANGE_NAME),
        key = {RabbitMQConstant.CREATE_ORDER_ROUTING_NAME}
))
public class MQServiceImpl implements MQService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StockService stockService;

    public MQServiceImpl(
            StockService stockService
    ) {
        this.stockService = stockService;
    }

    @RabbitHandler
    public void onReceivedCreateOrderMessage(Order order)  {
        stockService.saveOrder(order);
    }


}
