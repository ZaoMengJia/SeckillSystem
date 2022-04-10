package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.entity.Order;

import java.util.Map;

public interface OrderService {

    Boolean orderExist(String id);

    Map<String,Object> getOrder(int pageIndex, int pageSize);

    Map<String, Object> searchOrder(String keyword, int pageIndex, int pageSize);

    Order getOrderById(String id);

    Order getOrderByProductName(String productName);

    Order getOrderByUserName(String userName);

    void addOrder(Order order);

    void deleteOrder(String id);

    void updateOrder(Order order);


}
