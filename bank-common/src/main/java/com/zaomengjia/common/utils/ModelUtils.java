package com.zaomengjia.common.utils;

import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.OrderMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.service.FinancialProductSimpleService;
import com.zaomengjia.common.service.OrderSimpleService;
import com.zaomengjia.common.service.SaleProductDetailSimpleService;
import com.zaomengjia.common.service.StockSimpleService;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.bank.SaleProductVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 19:58
 */
@Component
public class ModelUtils {

    private final SeckillActivityMapper seckillActivityMapper;

    private final FinancialProductMapper financialProductMapper;

    private final OrderMapper orderMapper;

    public ModelUtils(SeckillActivityMapper seckillActivityMapper, FinancialProductMapper financialProductMapper, OrderMapper orderMapper) {
        this.seckillActivityMapper = seckillActivityMapper;
        this.financialProductMapper = financialProductMapper;
        this.orderMapper = orderMapper;
    }

    public SeckillActivityVO toSeckillActivityVO(SeckillActivity seckillActivity) {
        if(seckillActivity == null) {
            return null;
        }
        return new SeckillActivityVO()
                .setId(seckillActivity.getId())
                .setDetail(seckillActivity.getDetail())
                .setImage(seckillActivity.getImage())
                .setName(seckillActivity.getName())
                .setBeginTime(seckillActivity.getBeginTime())
                .setEndTime(seckillActivity.getEndTime());
    }


    public OrderVO toOrderVO(Order order) {
        OrderVO vo = new OrderVO()
                .setId(order.getId())
                .setStatus(order.getStatus())
                .setQuantity(order.getQuantity());
        vo.setSeckillActivity(
                toSeckillActivityVO(seckillActivityMapper.findById(order.getSeckillActivityId()).orElse(null))
        );

        vo.setFinancialProduct(
                toFinancialProductVO(financialProductMapper.findById(order.getFinancialProductId()).orElse(null))
        );

        return vo;
    }


    public FinancialProductVO toFinancialProductVO(FinancialProduct financialProduct) {
        if(financialProduct == null) {
            return null;
        }
        return new FinancialProductVO()
                .setId(financialProduct.getId())
                .setPrice((double) financialProduct.getPrice() / 100)
                .setName(financialProduct.getName());
    }

    public SaleProductVO toSaleProductVO(SaleProductDetail saleProductDetail) {
        if(saleProductDetail == null) {
            return null;
        }

        FinancialProduct financialProductEntity = financialProductMapper.findById(saleProductDetail.getFinancialProductId()).orElse(null);

        if(financialProductEntity == null) {
            return null;
        }

        return (SaleProductVO) new SaleProductVO()
                .setQuantity(saleProductDetail.getQuantity())
                .setId(financialProductEntity.getId())
                .setName(financialProductEntity.getName())
                .setPrice((double) financialProductEntity.getPrice() / 100);
    }
}
