package com.zaomengjia.bankmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.pojo.Order;
import com.zaomengjia.bankmanager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public Boolean orderExist(long oid) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oid",oid);
        return orderMapper.selectOne(queryWrapper)!=null;
    }

    @Override
    public Map<String, Object> getOrder(int pageIndex, int pageSize) {
        Page<Order> page = new Page<>(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>(5);
        orderMapper.selectPage(page, null);
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return map;
    }

    @Override
    public Order getOrderById(long oid) {
        return orderMapper.selectById(oid);
    }

    @Override
    public Order getOrderByUserName(String userName) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public Order getOrderByProductName(String productName) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("financial_product_name",productName);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public int addOrder(Order order) {
        return orderMapper.insert(order);
    }

    @Override
    public int deleteOrder(long oid) {
        return orderMapper.deleteById(oid);
    }

    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateById(order);
    }
}
