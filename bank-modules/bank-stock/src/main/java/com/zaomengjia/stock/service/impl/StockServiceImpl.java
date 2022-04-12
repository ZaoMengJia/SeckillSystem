package com.zaomengjia.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.zaomengjia.common.constant.OrderStatus;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.service.OrderSimpleService;
import com.zaomengjia.common.service.SaleProductDetailSimpleService;
import com.zaomengjia.common.service.StockSimpleService;
import com.zaomengjia.stock.service.StockService;
import com.zaomengjia.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 17:09
 */
@Service
@EnableAsync
public class StockServiceImpl implements StockService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrderMapper orderMapper;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final OrderSimpleService orderSimpleService;

    private final ConcurrentHashMap<String, SaleProductDetail> needUpdateDetailMap = new ConcurrentHashMap<>();

    private final StockSimpleService stockSimpleService;

    private final RedisUtils redisUtils;

    private final SaleProductDetailSimpleService saleProductDetailSimpleService;

    public StockServiceImpl(
            OrderMapper orderMapper,
            SaleProductDetailMapper saleProductDetailMapper,
            OrderSimpleService orderSimpleService,
            RedisUtils redisUtils,
            StockSimpleService stockSimpleService,
            SaleProductDetailSimpleService saleProductDetailSimpleService
    ) {
        this.orderMapper = orderMapper;
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.orderSimpleService = orderSimpleService;
        this.redisUtils = redisUtils;
        this.stockSimpleService = stockSimpleService;
        this.saleProductDetailSimpleService = saleProductDetailSimpleService;
    }



    public void saveOrder(Order order) {
        try {
            //减商品库存
            SaleProductDetail detail = saleProductDetailSimpleService.findByFinancialProductIdAndSeckillActivityId(order.getFinancialProductId(), order.getSeckillActivityId());
            if(detail == null || detail.getQuantity() < order.getQuantity()) {
                throw new Exception("无法创建订单：订单余量不满足需求");
            }


            boolean isSuccess = stockSimpleService.decrStock(order.getFinancialProductId(), order.getSeckillActivityId(), order.getQuantity());
            if(!isSuccess) {
                throw new Exception("无法创建订单：已售罄");
            }
            needUpdateDetailMap.put(order.getFinancialProductId()+ "::" + order.getSeckillActivityId(), detail);

            //创建订单
            order = orderMapper.save(order);

            //缓存重设
            order.setStatus(OrderStatus.NORMAL);
            orderSimpleService.setCache(order);
            logger.trace("订单{}创建成功 {}", order.getId(), Thread.currentThread().getName());
        }
        catch (Exception e) {
            //手动回滚
            orderMapper.deleteById(order.getId());
            logger.error("订单{}创建失败，订单内容为\n{}，错误", order.getId(), JSON.toJSONString(order), e);

            //设置订单失败
            order.setStatus(OrderStatus.ERROR);

            //错误信息4小时后删除
            orderSimpleService.setCache(order, 4 * 60 * 60);

            //开始回滚
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
