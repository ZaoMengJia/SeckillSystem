package com.zaomengjia.order.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.zaomengjia.common.constant.RabbitMQConstant;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.order.service.OrderService;
import com.zaomengjia.order.service.SeckillService;
import com.zaomengjia.order.utils.RedisUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/9 22:51
 */
@Service
@CacheConfig(cacheNames = "order-service")
public class OrderServiceImpl implements OrderService {

    private final RabbitTemplate rabbitTemplate;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final FinancialProductMapper financialProductMapper;

    private final SeckillService seckillService;

    private final OrderMapper orderMapper;

    private final RedisUtils redisUtils;

    public OrderServiceImpl(
            OrderMapper orderMapper,
            RabbitTemplate rabbitTemplate,
            RedisUtils redisUtils,
            SaleProductDetailMapper saleProductDetailMapper,
            FinancialProductMapper financialProductMapper,
            SeckillService seckillService
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderMapper = orderMapper;
        this.redisUtils = redisUtils;
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.financialProductMapper = financialProductMapper;
        this.seckillService = seckillService;

    }

    @Override
    public OrderVO modelToVO(Order order) {
        FinancialProduct financialProduct = getFinancialProductEntity(order.getFinancialProductId());
        SeckillActivity seckillActivity = seckillService.getSeckillActivityEntity(order.getSeckillActivityId());

        return new OrderVO()
                .setId(order.getId())
                .setCreateTime(order.getCreateTime())
                .setFinancialProduct(modelToVO(financialProduct))
                .setSeckillActivity(seckillService.modelToVO(seckillActivity))
                .setQuantity(order.getQuantity());
    }

    @Override
    public FinancialProductVO modelToVO(FinancialProduct financialProduct) {
        return new FinancialProductVO()
                .setId(financialProduct.getName())
                .setPrice((double) financialProduct.getPrice() / 100)
                .setName(financialProduct.getName());
    }

    @Override
    public PageVO<OrderVO> getUserOrderList(String userId, int pageNum, int pageSize) {
        Page<Order> entityPage = orderMapper.findByUserId(userId, PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::modelToVO));
    }

    @Override
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

    @Override
    public String createOrder(String userId, String seckillActivityId, String financialProductId) {
        //看看这个秒杀活动在不在
        SaleProductDetail saleProductDetail = getSaleProductDetailEntity(financialProductId, seckillActivityId);
        if(saleProductDetail == null) {
            throw new AppException(ResultCode.NO_SUCH_ACTIVITY_ERROR);
        }
        String saleProductDetailId = saleProductDetail.getId();

        //看看这个用户买过没有
        //这里就可以对买对数量判断了，目前是从订单数推购买数量，实在不行可以拿数据取和
        List<Order> orders = orderMapper.findByUserIdAndFinancialProductIdAndSeckillActivityId(userId, saleProductDetail.getFinancialProductId(), saleProductDetail.getSeckillActivityId());
        if(orders.size() >= 1) {
            throw new AppException(ResultCode.ORDER_EXCEED_LIMIT);
        }
        Set<String> orderKeys = redisUtils.keys("order::" + saleProductDetail.getId() + "::" + saleProductDetail.getFinancialProductId() + "::" + saleProductDetail.getSeckillActivityId() + "::" + userId);
        if(orderKeys.size() > 0) {
            throw new AppException(ResultCode.ORDER_EXCEED_LIMIT);
        }


        //从令牌桶找一个令牌
        Set<String> keys = redisUtils.keys("seckill-activity-token-bucket::" + saleProductDetailId + "::*");
        int tokenBucketSize = keys.size();
        if(tokenBucketSize == 0) {
            throw new AppException(ResultCode.SELL_OUT);
        }

        //尝试把令牌从令牌桶里拿出来
        //避免假死问题
        int tokenIndex = 0;
        if(tokenBucketSize > 5) {
            tokenIndex = new Random().nextInt(tokenBucketSize);
        }

        String token = new ArrayList<>(keys).get(tokenIndex);
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
        redisUtils.set("order::" + order.getId() + "::" + order.getFinancialProductId() + "::" +order.getSeckillActivityId() + "::" + userId, order);

        //创建消息队列，让库存服务进行持久化
        rabbitTemplate.convertAndSend(RabbitMQConstant.DEFAULT_EXCHANGE_NAME, RabbitMQConstant.CREATE_ORDER_ROUTING_NAME, order);

        //给前端一个订单号，供查询订单是否创建成功
        return order.getId();
    }

    @Override
    @Cacheable(cacheNames = "sale-product-detail-entity-by-id", key = "#saleProductDetailID")
    public SaleProductDetail getSaleProductDetailEntity(String saleProductDetailID) {
        return saleProductDetailMapper.findById(saleProductDetailID).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "sale-product-detail-entity-by-params", key = "#financialProductId + '::' + seckillActivityId")
    public SaleProductDetail getSaleProductDetailEntity(String financialProductId, String seckillActivityId) {
        return saleProductDetailMapper.findByFinancialProductIdAndSeckillActivityId(financialProductId, financialProductId);
    }

    @Override
    @Cacheable(cacheNames = "financial-product-entity", key = "#financialProductId")
    public FinancialProduct getFinancialProductEntity(String financialProductId) {
        return financialProductMapper.findById(financialProductId).orElse(null);
    }

    @Override
    public List<FinancialProduct> getFinancialProductEntityList(List<String> idList) {
        return financialProductMapper.findAllById(idList);
    }


}
