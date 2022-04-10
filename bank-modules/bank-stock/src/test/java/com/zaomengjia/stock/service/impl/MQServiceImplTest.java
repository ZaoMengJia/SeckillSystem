package com.zaomengjia.stock.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.zaomengjia.common.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 17:04
 */
@SpringBootTest
class MQServiceImplTest {
    @Autowired
    private MQServiceImpl mqService;

    @Test
    void test() throws InterruptedException {
        Order order = new Order();
        order.setStatus(false);
        order.setId(UuidUtils.generateUuid());
        order.setFinancialProductId("test");
        order.setSeckillActivityId("test");
        order.setQuantity(1);
        order.setUserId("test");
//        mqService.saveOrder(order);
    }
}