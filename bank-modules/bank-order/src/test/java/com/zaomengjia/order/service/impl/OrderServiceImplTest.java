package com.zaomengjia.order.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.zaomengjia.common.constant.OrderStatus;
import com.zaomengjia.common.constant.RabbitMQConstant;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 16:06
 */
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void test() {
        Order order = new Order();
        order.setStatus(OrderStatus.NORMAL);
        order.setId(UuidUtils.generateUuid());
        order.setFinancialProductId("test");
        order.setSeckillActivityId("test");
        order.setQuantity(1);
        order.setUserId("test");
//        rabbitTemplate.convertAndSend(RabbitMQConstant.DEFAULT_EXCHANGE_NAME, RabbitMQConstant.CREATE_ORDER_ROUTING_NAME, order);
    }
}