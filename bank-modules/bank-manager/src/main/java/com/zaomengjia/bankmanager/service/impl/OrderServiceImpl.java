package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.bankmanager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public Boolean orderExist(String id) {
        return orderMapper.findById(id).isPresent();
    }

    @Override
    public Map<String, Object> getOrder(int pageIndex, int pageSize) {
        return getPageInfo(pageIndex,pageSize);
    }

    private Map<String, Object> getPageInfo(int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>(5);
        Page<Order> page = orderMapper.findAll(PageRequest.of(pageIndex, pageSize));
        map.put("records", page.getContent());
        map.put("total", page.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> searchOrder(String keyword, int pageIndex, int pageSize) {
        Page<Order> result = orderMapper.findByUserIdContaining(keyword, PageRequest.of(pageIndex, pageSize));
        Map<String, Object> map = new HashMap<>(5);
        map.put("records", result.toList());
        map.put("total", result.getTotalElements());
        return map;
    }

    @Override
    public Order getOrderById(String id) {
        return orderMapper.findById(id).orElse(null);
    }

    @Override
    public Order getOrderByUserName(String userName) {
        return orderMapper.findByUserId(userName);
    }

    @Override
    public Order getOrderByProductName(String productName) {
        return orderMapper.findByFinancialProductId(productName);
    }

    @Override
    public void addOrder(Order order) {
        orderMapper.save(order);
    }

    @Override
    public void deleteOrder(String id) {
        orderMapper.deleteById(id);
    }

    @Override
    public void updateOrder(Order order) {
        orderMapper.save(order);
    }
}
