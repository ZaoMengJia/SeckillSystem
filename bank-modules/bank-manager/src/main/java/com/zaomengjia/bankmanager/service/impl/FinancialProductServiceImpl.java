package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.bankmanager.service.FinancialProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class FinancialProductServiceImpl implements FinancialProductService {
    @Autowired
    FinancialProductMapper financialProductMapper;

    @Override
    public Boolean financialProductExist(String fname) {
        return financialProductMapper.findByName(fname) != null;
    }

    /**
     * 获取全部商品
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> getFinancialProduct(int pageIndex, int pageSize) {
        Page<FinancialProduct> page = financialProductMapper.findByNameContaining("", PageRequest.of(pageIndex, pageSize));
        Map<String, Object> map = new HashMap<>(5);
        map.put("records", page.getContent());
        map.put("total", page.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> searchProduct(String keyword, int pageIndex, int pageSize) {
        Page<FinancialProduct> page = financialProductMapper.findByNameContaining(keyword, PageRequest.of(pageIndex, pageSize));
        Map<String, Object> map = new HashMap<>(5);
        map.put("records", page.getContent());
        map.put("total", page.getTotalElements());
        return map;
    }


    @Override
    public FinancialProduct getFinancialProductById(String fpid) {
        return financialProductMapper.findById(fpid).orElse(null);
    }

    @Override
    public FinancialProduct getFinancialProductByPrice(int price) {
        return financialProductMapper.findByPrice(price);
    }

    @Override
    public FinancialProduct getFinancialProductByName(String fname) {
        return financialProductMapper.findByName(fname);
    }

    @Override
    public void addFinancialProduct(FinancialProduct financialProduct) {
        financialProductMapper.save(financialProduct);
    }

    @Override
    public void deleteFinancialProduct(String fpid) {
         financialProductMapper.deleteById(fpid);
    }

    @Override
    public void updateFinancialProduct(FinancialProduct financialProduct) {
        financialProductMapper.save(financialProduct);
    }
}
