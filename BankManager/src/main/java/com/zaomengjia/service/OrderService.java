package com.zaomengjia.service;

import com.zaomengjia.pojo.Order;

import java.util.Map;

public interface OrderService {

    Boolean orderExist(long oid);

    Map<String,Object> getOrder(int pageIndex, int pageSize);

    Order getOrderById(long oid);

    Order getOrderByProductName(String productName);

    Order getOrderByUserName(String userName);

    int addOrder(Order order);

    int deleteOrder(long oid);

    int updateOrder(Order order);


}
