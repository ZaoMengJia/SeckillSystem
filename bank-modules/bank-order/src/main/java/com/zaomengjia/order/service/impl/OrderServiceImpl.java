package com.zaomengjia.order.service.impl;

import cn.hutool.core.lang.UUID;
import com.zaomengjia.common.constant.OrderStatus;
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
import com.zaomengjia.common.service.FinancialProductSimpleService;
import com.zaomengjia.common.service.OrderSimpleService;
import com.zaomengjia.common.service.SaleProductDetailSimpleService;
import com.zaomengjia.common.service.StockSimpleService;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.order.service.OrderService;
import com.zaomengjia.order.service.SeckillService;
import com.zaomengjia.common.utils.RedisUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/9 22:51
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final RabbitTemplate rabbitTemplate;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final FinancialProductMapper financialProductMapper;

    private final SeckillService seckillService;

    private final OrderMapper orderMapper;

    private final RedisUtils redisUtils;

    private final OrderSimpleService orderSimpleService;

    private final SaleProductDetailSimpleService saleProductDetailSimpleService;

    private final FinancialProductSimpleService financialProductSimpleService;

    private final StockSimpleService stockSimpleService;

    private final ThreadPoolExecutor rabbitMqAsyncServiceExecutor;

    public OrderServiceImpl(
            OrderMapper orderMapper,
            RabbitTemplate rabbitTemplate,
            RedisUtils redisUtils,
            SaleProductDetailMapper saleProductDetailMapper,
            FinancialProductMapper financialProductMapper,
            OrderSimpleService orderSimpleService,
            SaleProductDetailSimpleService saleProductDetailSimpleService,
            SeckillService seckillService,
            FinancialProductSimpleService financialProductSimpleService,
            StockSimpleService stockSimpleService,
            ThreadPoolExecutor rabbitMqAsyncServiceExecutor
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderMapper = orderMapper;
        this.redisUtils = redisUtils;
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.financialProductMapper = financialProductMapper;
        this.seckillService = seckillService;
        this.orderSimpleService = orderSimpleService;
        this.saleProductDetailSimpleService = saleProductDetailSimpleService;
        this.financialProductSimpleService = financialProductSimpleService;
        this.stockSimpleService = stockSimpleService;
        this.rabbitMqAsyncServiceExecutor = rabbitMqAsyncServiceExecutor;
    }

    @Override
    public OrderVO modelToVO(Order order) {
        if(order == null) {
            return null;
        }
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
    public PageVO<OrderVO> getUserOrderList(String userId) {
        Page<Order> entityPage = orderMapper.findByUserId(userId, null);
        return new PageVO<>(entityPage.map(this::modelToVO));
    }

    @Override
    public PageVO<OrderVO> getUserOrderList(String userId, int pageNum, int pageSize) {
        Page<Order> entityPage = orderMapper.findByUserId(userId, PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::modelToVO));
    }

    @Override
    public Order getOrder(String orderId) {
        Order order = orderSimpleService.getCacheById(orderId);
        if(order == null) {
            order = orderMapper.findById(orderId).orElse(null);
            if(order == null) {
                return null;
            }
            orderSimpleService.setCache(order);
        }

        return order;
    }

    @Override
    public String createOrder(String userId, String seckillActivityId, String financialProductId) {
        //看看这个秒杀活动在不在
        SaleProductDetail saleProductDetail = getSaleProductDetailEntity(financialProductId, seckillActivityId);

        if(saleProductDetail == null) {
            throw new AppException(ResultCode.NO_SUCH_ACTIVITY_ERROR);
        }

        //组建订单
        Order order = new Order();
        order.setFinancialProductId(saleProductDetail.getFinancialProductId());
        order.setSeckillActivityId(saleProductDetail.getSeckillActivityId());
        order.setUserId(userId);
        order.setQuantity(1);

        //看看这个用户买过没有
        //这里就可以对买对数量判断了，目前是从订单数推购买数量，实在不行可以拿数据取和
        //Todo: 压测暂时注释
//        List<Order> orders = orderMapper.findByUserIdAndFinancialProductIdAndSeckillActivityId(userId, saleProductDetail.getFinancialProductId(), saleProductDetail.getSeckillActivityId());
//        if(orders.size() >= 1) {
//            throw new AppException(ResultCode.ORDER_EXCEED_LIMIT);
//        }
//        if(orderSimpleService.containCache(order)) {
//            throw new AppException(ResultCode.ORDER_EXCEED_LIMIT);
//        }

//        long l1 = System.currentTimeMillis();
//        LoggerFactory.getLogger(getClass()).info("=> {}", System.currentTimeMillis() - l1);

        //尝试把一个令牌从令牌桶里拿出来
        if(stockSimpleService.attemptGetToken(financialProductId, seckillActivityId) == null) {
            //消费失败
            throw new AppException(ResultCode.SELL_OUT);
        }

        //拿到了令牌，开始创建订单
        order.setId(UUID.fastUUID().toString(true));
        order.setStatus(OrderStatus.CREATING);

        //预订单保存在redis中
        orderSimpleService.setCache(order);

        //创建消息队列，让库存服务进行持久化
        rabbitMqAsyncServiceExecutor.execute(() -> {
            rabbitTemplate.convertAndSend(RabbitMQConstant.DEFAULT_EXCHANGE_NAME, RabbitMQConstant.CREATE_ORDER_ROUTING_NAME, order);
        });

        //给前端一个订单号，供查询订单是否创建成功
        return order.getId();
    }

    @Override
    public SaleProductDetail getSaleProductDetailEntity(String saleProductDetailID) {
        return saleProductDetailSimpleService.findById(saleProductDetailID);
    }

    @Override
    public SaleProductDetail getSaleProductDetailEntity(String financialProductId, String seckillActivityId) {
        return saleProductDetailSimpleService.findByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId);
    }

    @Override
    public FinancialProduct getFinancialProductEntity(String financialProductId) {
        return financialProductSimpleService.getFinancialProductEntity(financialProductId);
    }

    @Override
    public List<FinancialProduct> getFinancialProductEntityList(List<String> idList) {
        return financialProductMapper.findAllById(idList);
    }

    @Override
    public OrderVO getOrderDetail(String id) {
        Order order = getOrder(id);
        return modelToVO(order);
    }

    @Override
    public OrderStatus getOrderStatus(String id) {
        Order order = getOrder(id);
        return order.getStatus();
    }


}
