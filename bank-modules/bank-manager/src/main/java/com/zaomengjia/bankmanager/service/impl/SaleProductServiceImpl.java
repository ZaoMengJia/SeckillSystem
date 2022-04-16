package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.service.SaleProductService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.service.SaleProductDetailSimpleService;
import com.zaomengjia.common.utils.ModelUtils;
import com.zaomengjia.common.vo.bank.SaleProductVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/16 00:05
 */
@Service
public class SaleProductServiceImpl implements SaleProductService {

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final ModelUtils modelUtils;

    private final FinancialProductMapper financialProductMapper;

    private final SaleProductDetailSimpleService saleProductDetailSimpleService;

    public SaleProductServiceImpl(
            SaleProductDetailMapper saleProductDetailMapper,
            ModelUtils modelUtils,
            FinancialProductMapper financialProductMapper,
            SaleProductDetailSimpleService saleProductDetailSimpleService
    ) {
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.financialProductMapper = financialProductMapper;
        this.modelUtils = modelUtils;
        this.saleProductDetailSimpleService = saleProductDetailSimpleService;
    }

    @Override
    public List<SaleProductVO> getList(String seckillActivityId) {
        List<SaleProductDetail> list = saleProductDetailMapper.findBySeckillActivityId(seckillActivityId);
        list.forEach(saleProductDetailSimpleService::setCache);

        List<FinancialProduct> productList = financialProductMapper.findAllById(list.stream().map(SaleProductDetail::getId).collect(Collectors.toList()));
        Map<String, SaleProductDetail> map = list.stream().collect(Collectors.toMap(
                SaleProductDetail::getFinancialProductId,
                a -> a
        ));

        return productList.stream().map(p -> {
            SaleProductVO saleProductVO = new SaleProductVO();
            SaleProductDetail detail = map.get(p.getId());
            saleProductVO.setId(detail.getId());
            saleProductVO.setPrice((double) p.getPrice() / 100);
            saleProductVO.setName(p.getName());
            saleProductVO.setQuantity(detail.getQuantity());
            return saleProductVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void insert(String financialProductId, String seckillActivityId, int quantity, int total) {

        financialProductMapper.findById(financialProductId).orElseThrow(() -> new AppException(ResultCode.NO_SUCH_PRODUCT_ERROR));

        SaleProductDetail detail = saleProductDetailSimpleService.findByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId);
        if(detail != null) {
            throw new AppException(ResultCode.SALE_PRODUCT_EXISTED);
        }

        SaleProductDetail entity = new SaleProductDetail();
        entity.setFinancialProductId(financialProductId);
        entity.setSeckillActivityId(seckillActivityId);
        entity.setQuantity(quantity);
        entity.setTotal(total);
        saleProductDetailSimpleService.save(entity);

    }

    @Override
    public void delete(String financialProductId, String seckillActivityId) {
        saleProductDetailSimpleService.deleteByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId);
    }

    @Override
    public void modify(String financialProductId, String seckillActivityId, int quantity, int total) {
        saleProductDetailSimpleService.modifyQuantityAndTotal(financialProductId, seckillActivityId, quantity, total);


    }


}
