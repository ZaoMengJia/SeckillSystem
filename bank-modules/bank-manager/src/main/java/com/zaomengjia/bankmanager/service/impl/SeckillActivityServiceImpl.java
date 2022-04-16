package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.dto.SeckillActivityDto;
import com.zaomengjia.bankmanager.service.SeckillActivityService;
import com.zaomengjia.bankmanager.vo.SeckillActivityWithProductListVO;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.service.SeckillActivitySimpleService;
import com.zaomengjia.common.service.StockSimpleService;
import com.zaomengjia.common.utils.ModelUtils;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.SaleProductVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.page.PageVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 23:19
 */
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {

    private final SeckillActivityMapper seckillActivityMapper;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final FinancialProductMapper financialProductMapper;

    private final StockSimpleService stockSimpleService;

    private final ModelUtils modelUtils;

    private final SeckillActivitySimpleService seckillActivitySimpleService;

    public SeckillActivityServiceImpl(
            SeckillActivityMapper seckillActivityMapper,
            ModelUtils modelUtils,
            SaleProductDetailMapper saleProductDetailMapper,
            FinancialProductMapper financialProductMapper,
            StockSimpleService stockSimpleService,
            SeckillActivitySimpleService seckillActivitySimpleService
    ) {
        this.seckillActivityMapper = seckillActivityMapper;
        this.modelUtils = modelUtils;
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.financialProductMapper = financialProductMapper;
        this.stockSimpleService = stockSimpleService;
        this.seckillActivitySimpleService = seckillActivitySimpleService;
    }


    private SeckillActivityWithProductListVO getVO(SeckillActivity seckillActivity) {
        List<SaleProductDetail> list = saleProductDetailMapper.findBySeckillActivityId(seckillActivity.getId());
        List<FinancialProduct> productList = financialProductMapper.findAllById(list.stream().map(SaleProductDetail::getId).collect(Collectors.toList()));
        Map<String, SaleProductDetail> map = list.stream().collect(Collectors.toMap(
                SaleProductDetail::getFinancialProductId,
                a -> a
        ));

        List<SaleProductVO> saleProductList = productList.stream().map(p -> {
            SaleProductVO saleProductVO = new SaleProductVO();
            SaleProductDetail detail = map.get(p.getId());
            saleProductVO.setId(detail.getId());
            saleProductVO.setPrice((double) p.getPrice() / 100);
            saleProductVO.setName(p.getName());
            saleProductVO.setQuantity(detail.getQuantity());
            return saleProductVO;
        }).collect(Collectors.toList());

        SeckillActivityWithProductListVO vo = new SeckillActivityWithProductListVO();
        vo.setName(seckillActivity.getName());
        vo.setDetail(seckillActivity.getDetail());
        vo.setImage(seckillActivity.getImage());
        vo.setBeginTime(seckillActivity.getBeginTime());
        vo.setEndTime(seckillActivity.getEndTime());
        vo.setProductList(saleProductList);

        return vo;
    }

    @Override
    public PageVO<SeckillActivityVO> getList(int pageNum, int pageSize) {
        Page<SeckillActivity> page = seckillActivityMapper.findAll(PageRequest.of(pageNum - 1, pageSize));
        seckillActivitySimpleService.setCache(page.getContent());
        return new PageVO<>(page.map(modelUtils::toSeckillActivityVO));
    }

    @Override
    public PageVO<SeckillActivityWithProductListVO> getListWithDetail(int pageNum, int pageSize) {
        Page<SeckillActivity> page = seckillActivityMapper.findAll(PageRequest.of(pageNum - 1, pageSize));
        seckillActivitySimpleService.setCache(page.getContent());
        return new PageVO<>(page.map(this::getVO));
    }

    @Override
    public PageVO<SeckillActivityWithProductListVO> searchByName(String name, int pageNum, int pageSize) {
        Page<SeckillActivity> page = seckillActivityMapper.findByNameContaining(name, PageRequest.of(pageNum - 1, pageSize));
        seckillActivitySimpleService.setCache(page.getContent());
        return new PageVO<>(page.map(this::getVO));
    }

    @Override
    public String create(SeckillActivityDto dto) {
        SeckillActivity entity = new SeckillActivity();
        entity.setName(dto.getName());
        entity.setDetail(dto.getDetail());
        entity.setBeginTime(dto.getBeginTime());
        entity.setEndTime(dto.getEndTime());
        entity.setImage(dto.getImage());

        entity = seckillActivityMapper.save(entity);
        seckillActivitySimpleService.setCache(entity);
        return entity.getId();
    }

    @Override
    public void modify(String id, SeckillActivityDto dto) {
        SeckillActivity entity = new SeckillActivity();
        entity.setId(id);
        entity.setName(dto.getName());
        entity.setDetail(dto.getDetail());
        entity.setBeginTime(dto.getBeginTime());
        entity.setEndTime(dto.getEndTime());
        entity.setImage(dto.getImage());

        seckillActivityMapper.save(entity);
        seckillActivitySimpleService.setCache(entity);
    }

    @Override
    public void delete(String id) {
        try {
            seckillActivityMapper.deleteById(id);
            List<Pair<String, String>> keyList = saleProductDetailMapper.findBySeckillActivityId(id).stream().map(c -> Pair.of(c.getFinancialProductId(), c.getSeckillActivityId())).collect(Collectors.toList());
            stockSimpleService.deleteStockBatch(keyList);
            stockSimpleService.deleteTokenBucketBatch(keyList);
            saleProductDetailMapper.deleteBySeckillActivityId(id);
        }
        catch (Exception ignored) {}
    }

    @Override
    public SeckillActivityWithProductListVO getDetail(String seckillActivityId) {
        SeckillActivity seckillActivity = seckillActivityMapper.findById(seckillActivityId).orElse(null);
        if(seckillActivity == null) {
            return null;
        }

        SeckillActivityWithProductListVO vo = getVO(seckillActivity);
        return vo;
    }
}
