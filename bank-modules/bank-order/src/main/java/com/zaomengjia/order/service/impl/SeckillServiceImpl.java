package com.zaomengjia.order.service.impl;

import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.vo.bank.SaleProductVO;
import com.zaomengjia.common.vo.bank.SeckillActivityDetailVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.order.service.OrderService;
import com.zaomengjia.order.service.SeckillService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 18:17
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private final SeckillActivityMapper seckillActivityMapper;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final OrderService orderService;

    public SeckillServiceImpl(
            SeckillActivityMapper seckillActivityMapper,
            @Lazy OrderService orderService,
            SaleProductDetailMapper saleProductDetailMapper
    ) {
        this.seckillActivityMapper = seckillActivityMapper;
        this.orderService = orderService;
        this.saleProductDetailMapper = saleProductDetailMapper;
    }

    @Override
    public SeckillActivityVO modelToVO(SeckillActivity seckillActivity) {
        return new SeckillActivityVO()
                .setId(seckillActivity.getId())
                .setDetail(seckillActivity.getDetail())
                .setImage(seckillActivity.getImage())
                .setName(seckillActivity.getName())
                .setBeginTime(seckillActivity.getBeginTime())
                .setEndTime(seckillActivity.getEndTime());
    }

    public SaleProductVO modelToVO(SaleProductDetail saleProductDetail) {
        FinancialProduct financialProductEntity = orderService.getFinancialProductEntity(saleProductDetail.getFinancialProductId());
        return (SaleProductVO) new SaleProductVO()
                .setQuantity(saleProductDetail.getQuantity())
                .setId(financialProductEntity.getId())
                .setName(financialProductEntity.getName())
                .setPrice((double) financialProductEntity.getPrice() / 100);
    }

    public List<SaleProductVO> modelToVO(List<SaleProductDetail> list) {
        Map<String, FinancialProduct> idFinancialProductMap = orderService.getFinancialProductEntityList(list.stream().map(SaleProductDetail::getFinancialProductId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(FinancialProduct::getId, c -> c));
        return list.stream().map(c -> {
            FinancialProduct financialProduct = idFinancialProductMap.get(c.getFinancialProductId());
            return (SaleProductVO) new SaleProductVO()
                    .setQuantity(c.getQuantity())
                    .setPrice((double) financialProduct.getPrice() / 100)
                    .setId(financialProduct.getId())
                    .setName(financialProduct.getName());
        }).collect(Collectors.toList());
    }

    @Override
    public SeckillActivityVO getSeckillActivityById(String id) {
        Optional<SeckillActivity> optional = seckillActivityMapper.findById(id);
        return optional.map(this::modelToVO).orElse(null);
    }

    @Override
    public SeckillActivityDetailVO getSeckillActivityDetail(String id) {
        Optional<SeckillActivity> optional = seckillActivityMapper.findById(id);
        if(!optional.isPresent()) {
            return null;
        }
        SeckillActivity seckillActivity = optional.get();
        SeckillActivityDetailVO result = new SeckillActivityDetailVO();

        List<SaleProductDetail> saleProductDetail = saleProductDetailMapper.findBySeckillActivityId(id);

        result.setProductList(modelToVO(saleProductDetail));
        result.setId(seckillActivity.getId());
        result.setName(seckillActivity.getName());
        result.setDetail(seckillActivity.getDetail());
        result.setImage(seckillActivity.getImage());
        result.setBeginTime(seckillActivity.getBeginTime());
        result.setEndTime(seckillActivity.getEndTime());

        return result;
    }

    @Cacheable(cacheNames = "seckill-activity-entity", key = "#seckillActivityId")
    public SeckillActivity getSeckillActivityEntity(String seckillActivityId) {
        return seckillActivityMapper.findById(seckillActivityId).orElse(null);
    }

    @Override
    public PageVO<SeckillActivityVO> getSeckillActivityList(int pageNum, int pageSize) {
        Page<SeckillActivity> entityPage = seckillActivityMapper.findAll(PageRequest.of(pageNum, pageSize));
        return new PageVO<>(entityPage.map(this::modelToVO));
    }
}
