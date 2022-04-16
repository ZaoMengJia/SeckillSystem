//package com.zaomengjia.bankmanager.service.impl;
//
//import com.zaomengjia.common.dao.OrderMapper;
//import com.zaomengjia.common.entity.Order;
//import com.zaomengjia.bankmanager.service.OrderService;
//import com.zaomengjia.common.vo.page.PageVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class OldOrderServiceImpl implements OrderService {
//
//    @Autowired
//    OrderMapper orderMapper;
//
//    @Override
//    public Boolean orderExist(String id) {
//        return orderMapper.findById(id).isPresent();
//    }
//
//    @Override
//    public PageVO<Order> getOrder(int pageIndex, int pageSize) {
//        return getPageInfo(pageIndex,pageSize);
//    }
//
//    private PageVO<Order> getPageInfo(int pageIndex, int pageSize) {
//        Page<Order> page = orderMapper.findAll(PageRequest.of(pageIndex - 1, pageSize));
//        return new PageVO<>(page);
//    }
//
//    @Override
//    public PageVO<Order> searchOrder(String keyword, int pageIndex, int pageSize) {
//        Page<Order> result = orderMapper.findByUserIdContaining(keyword, PageRequest.of(pageIndex - 1, pageSize));
//        return new PageVO<>(result);
//    }
//
//    @Override
//    public Order getOrderById(String id) {
//        return orderMapper.findById(id).orElse(null);
//    }
//
//    @Override
//    public Order getOrderByUserName(String userName) {
//        return orderMapper.findByUserId(userName);
//    }
//
//    @Override
//    public Order getOrderByProductName(String productName) {
//        return orderMapper.findByFinancialProductId(productName);
//    }
//
//    @Override
//    public void addOrder(Order order) {
//        orderMapper.save(order);
//    }
//
//    @Override
//    public void deleteOrder(String id) {
//        orderMapper.deleteById(id);
//    }
//
//    @Override
//    public void updateOrder(Order order) {
//        orderMapper.save(order);
//    }
//}
