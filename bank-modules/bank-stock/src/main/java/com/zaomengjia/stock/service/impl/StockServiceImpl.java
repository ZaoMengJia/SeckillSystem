package com.zaomengjia.stock.service.impl;

import com.alibaba.fastjson.JSON;
import com.zaomengjia.common.constant.OrderStatus;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.stock.service.StockService;
import com.zaomengjia.stock.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 17:09
 */
@Service
public class StockServiceImpl implements StockService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrderMapper orderMapper;

    private final RedisUtils redisUtils;

    private final SaleProductDetailMapper saleProductDetailMapper;

    public StockServiceImpl(
            OrderMapper orderMapper,
            RedisUtils redisUtils,
            SaleProductDetailMapper saleProductDetailMapper
    ) {
        this.orderMapper = orderMapper;
        this.redisUtils = redisUtils;
        this.saleProductDetailMapper = saleProductDetailMapper;
    }

    @Transactional
    public void saveOrder(Order order) {
        try {
            //减商品库存
            SaleProductDetail detail = saleProductDetailMapper.findByFinancialProductIdAndSeckillActivityId(order.getFinancialProductId(), order.getSeckillActivityId());
            if(detail == null || detail.getQuantity() < order.getQuantity()) {
                throw new Exception();
            }

            detail.setQuantity(detail.getQuantity() - order.getQuantity());
            saleProductDetailMapper.save(detail);

            //创建订单
            order = orderMapper.save(order);

            //缓存重设
            order.setStatus(OrderStatus.NORMAL);
            redisUtils.set("order::" + order.getId() + "::" + order.getFinancialProductId() + "::" +order.getSeckillActivityId() + "::" + order.getUserId(), order);
            logger.debug("订单{}创建成功", order.getId());
        }
        catch (Exception e) {
            logger.error("订单{}创建失败，订单内容为\n{}，错误", order.getId(), JSON.toJSONString(order), e);

            //设置订单失败
            order.setStatus(OrderStatus.ERROR);
            String key = "order::" + order.getId() + "::" + order.getFinancialProductId() + "::" +order.getSeckillActivityId() + "::" + order.getUserId();
            //错误信息4小时后删除
            redisUtils.set(key, order, 4 * 60 * 60);



            //开始回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
