package com.zaomengjia.order.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.zaomengjia.common.constant.RabbitMQConstant;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.vo.order.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.order.utils.RedisUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/9 22:51
 */
@Service
@CacheConfig(cacheNames = "order-service")
public class OrderServiceImpl{

    private final RabbitTemplate rabbitTemplate;

    private final SeckillActivityMapper seckillActivityMapper;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final FinancialProductMapper financialProductMapper;

    private final OrderMapper orderMapper;

    private final RedisUtils redisUtils;

    public OrderServiceImpl(
            OrderMapper orderMapper,
            RabbitTemplate rabbitTemplate,
            RedisUtils redisUtils,
            SeckillActivityMapper seckillActivityMapper,
            SaleProductDetailMapper saleProductDetailMapper,
            FinancialProductMapper financialProductMapper
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.seckillActivityMapper = seckillActivityMapper;
        this.orderMapper = orderMapper;
        this.redisUtils = redisUtils;
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.financialProductMapper = financialProductMapper;
    }

    public PageVO<OrderVO> getUserOrderList(String userId, int pageNum, int pageSize) {
        Page<Order> entityPage = orderMapper.findByUserId(userId, PageRequest.of(pageNum, pageSize));

        //Todo: 视图类转换还没写
        return new PageVO<>(entityPage.map(c -> new OrderVO()));
    }

    public Order getOrder(String orderId) {
        Order s = (Order) redisUtils.get("order:" + orderId);
        if(s == null) {
            Order order = orderMapper.findById(orderId).orElse(null);
            if(order == null) {
                return null;
            }
            redisUtils.set("order:" + orderId, order);
        }
        return s;
    }

    public String createOrder(String userId, String saleProductDetailId) {
        //看看这个秒杀活动在不在
        SaleProductDetail saleProductDetail = getSaleProductDetail(saleProductDetailId);
        if(saleProductDetail == null) {
            throw new AppException(ResultCode.NO_SUCH_ACTIVITY_ERROR);
        }

        //从令牌桶找一个令牌
        Set<String> keys = redisUtils.keys("seckill-activity-token-bucket::" + saleProductDetailId + "::*");
        if(keys == null || keys.size() == 0) {
            throw new AppException(ResultCode.SELL_OUT);
        }

        //尝试把令牌从令牌桶里拿出来
        String token = new ArrayList<>(keys).get(0);
        boolean isSuccess = redisUtils.del(token);

        if(!isSuccess) {
            //没有成功，交易失败，让用户重新尝试
            throw new AppException(ResultCode.CREATE_ORDER_ERROR);
        }

        //拿到了令牌，开始创建订单
        Order order = new Order();
        order.setPersistent(false);
        order.setId(UuidUtils.generateUuid());
        order.setFinancialProductId(saleProductDetail.getFinancialProductId());
        order.setSeckillActivityId(saleProductDetail.getSeckillActivityId());
        order.setQuantity(1);
        order.setUserId(userId);

        //预订单保存在redis中
        redisUtils.set("order::" + order.getId(), order);

        //创建消息队列，让库存服务进行持久化
        rabbitTemplate.convertAndSend(RabbitMQConstant.DEFAULT_EXCHANGE_NAME, RabbitMQConstant.CREATE_ORDER_ROUTING_NAME, order);

        //给前端一个订单号，供查询订单是否创建成功
        return order.getId();
    }

    @Cacheable(cacheNames = "sale-product-detail-entity", key = "#saleProductDetailID")
    public SaleProductDetail getSaleProductDetail(String saleProductDetailID) {
        return saleProductDetailMapper.findById(saleProductDetailID).orElse(null);
    }


}
