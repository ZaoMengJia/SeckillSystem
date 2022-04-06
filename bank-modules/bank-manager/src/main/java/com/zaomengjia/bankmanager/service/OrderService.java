package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.pojo.Order;

import java.util.Map;

public interface OrderService {

    Boolean orderExist(long oid);

    Map<String,Object> getOrder(int pageIndex, int pageSize);

    Map<String, Object> searchOrder(String keyword, int pageIndex, int pageSize);

    Order getOrderById(long oid);

    Order getOrderByProductName(String productName);

    Order getOrderByUserName(String userName);

    void addOrder(Order order);

    void deleteOrder(long oid);

    void updateOrder(Order order);


}
