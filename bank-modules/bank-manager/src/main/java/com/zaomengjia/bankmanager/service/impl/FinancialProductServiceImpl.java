package com.zaomengjia.bankmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.pojo.FinancialProduct;
import com.zaomengjia.bankmanager.service.FinancialProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class FinancialProductServiceImpl implements FinancialProductService {
    @Autowired
    FinancialProductMapper financialProductMapper;

    @Override
    public Boolean financialProductExist(String fname) {
        QueryWrapper<FinancialProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fname",fname);
        return financialProductMapper.selectOne(queryWrapper)!=null;
    }

    @Override
    public Map<String, Object> getFinancialProduct(int pageIndex, int pageSize) {
        return getPageInfo(pageIndex,pageSize,null);
    }

    private Map<String, Object> getPageInfo(int pageIndex, int pageSize, QueryWrapper<FinancialProduct> queryWrapper) {
        Page<FinancialProduct> page = new Page<>(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>(5);
        financialProductMapper.selectPage(page, queryWrapper);
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return map;
    }

    @Override
    public FinancialProduct getFinancialProductById(long fpid) {
        return financialProductMapper.selectById(fpid);
    }

    @Override
    public FinancialProduct getFinancialProductByPrice(int price) {
        QueryWrapper<FinancialProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("price",price);
        return financialProductMapper.selectOne(queryWrapper);
    }

    @Override
    public FinancialProduct getFinancialProductByName(String fname) {
        return financialProductMapper.selectOne(new QueryWrapper<FinancialProduct>().eq("fname",fname));
    }

    @Override
    public int addFinancialProduct(FinancialProduct financialProduct) {
        return financialProductMapper.insert(financialProduct);
    }

    @Override
    public int deleteFinancialProduct(String fpid) {
        return financialProductMapper.deleteById(fpid);
    }

    @Override
    public int updateFinancialProduct(FinancialProduct financialProduct) {
        return financialProductMapper.updateById(financialProduct);
    }
}
