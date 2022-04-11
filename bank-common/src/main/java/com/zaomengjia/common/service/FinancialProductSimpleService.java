package com.zaomengjia.common.service;

import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 01:14
 */
@Service
@Transactional
@CacheConfig(cacheNames = "financial-product")
public class FinancialProductSimpleService {

    private final FinancialProductMapper financialProductMapper;

    public FinancialProductSimpleService(FinancialProductMapper financialProductMapper) {
        this.financialProductMapper = financialProductMapper;
    }


    @Cacheable(cacheNames = "financial-product-entity", key = "#financialProductId")
    public FinancialProduct getFinancialProductEntity(String financialProductId) {
        return financialProductMapper.findById(financialProductId).orElse(null);
    }
}
