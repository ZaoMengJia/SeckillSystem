package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.service.OrderService;
import com.zaomengjia.bankmanager.vo.OrderVO;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.dao.WeixinUserMapper;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.utils.ModelUtils;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 20:21
 */
@Service
public class OrderServiceImpl implements OrderService {
    public final ModelUtils modelUtils;

    public final OrderMapper orderMapper;

    private final SeckillActivityMapper seckillActivityMapper;

    private final FinancialProductMapper financialProductMapper;

    private final WeixinUserMapper weixinUserMapper;

    public OrderServiceImpl(
            ModelUtils modelUtils,
            OrderMapper orderMapper,
            SeckillActivityMapper seckillActivityMapper,
            FinancialProductMapper financialProductMapper,
            WeixinUserMapper weixinUserMapper
    ) {
        this.modelUtils = modelUtils;
        this.orderMapper = orderMapper;
        this.seckillActivityMapper = seckillActivityMapper;
        this.financialProductMapper = financialProductMapper;
        this.weixinUserMapper = weixinUserMapper;
    }

    private OrderVO toVO(Order order) {
        OrderVO vo = new OrderVO()
                .setId(order.getId())
                .setStatus(order.getStatus())
                .setQuantity(order.getQuantity());
        vo.setSeckillActivity(
                modelUtils.toSeckillActivityVO(seckillActivityMapper.findById(order.getSeckillActivityId()).orElse(null))
        );

        vo.setFinancialProduct(
                modelUtils.toFinancialProductVO(financialProductMapper.findById(order.getFinancialProductId()).orElse(null))
        );

        WeixinUserVO weixinUserVO = new WeixinUserVO();
        WeixinUser weixinUser = weixinUserMapper.findById(order.getUserId()).orElse(null);
        if(weixinUser != null) {
            BeanUtils.copyProperties(weixinUser, weixinUserVO);
            vo.setWeixinUser(weixinUserVO);
        }

        return vo;
    }

    @Override
    public PageVO<OrderVO> getOrderList(int pageNum, int pageSize) {
        Page<Order> page = orderMapper.findAll(PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(page.map(this::toVO));
    }

    @Override
    public PageVO<OrderVO> searchOrderById(String keyword, int pageNum, int pageSize) {
        Page<Order> page = orderMapper.findByIdContaining(keyword, PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(page.map(this::toVO));
    }
}
