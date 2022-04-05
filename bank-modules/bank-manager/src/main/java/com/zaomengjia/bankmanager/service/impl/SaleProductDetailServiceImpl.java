package com.zaomengjia.bankmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.pojo.SaleProductDetail;
import com.zaomengjia.bankmanager.service.SaleProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaleProductDetailServiceImpl implements SaleProductDetailService {

    @Autowired
    SaleProductDetailMapper saleProductDetailMapper;

    @Override
    public Boolean saleProductDetailExist(long said,long fpid) {
        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seckill_activity_id",said).eq("financial_product_id",fpid);
        return saleProductDetailMapper.selectOne(queryWrapper)!=null;
    }

    @Override
    public Map<String, Object> getSaleProductDetail(int pageIndex,int pageSize) {
        Page<SaleProductDetail> page = new Page<>(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>(5);
        saleProductDetailMapper.selectPage(page, null);
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> searchDetail(String keyword, int pageIndex, int pageSize) {
        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("seckill_activity_id", keyword);
        Page<SaleProductDetail> page = new Page<>(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>(5);
        Page<SaleProductDetail> result = saleProductDetailMapper.selectPage(page, queryWrapper);
        map.put("records", result.getRecords());
        map.put("total", result.getTotal());
        return map;
    }

    @Override
    public SaleProductDetail getSaleProductDetailBySaid(long said) {
        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seckill_activity_id",said);
        return saleProductDetailMapper.selectOne(queryWrapper);
    }

    @Override
    public SaleProductDetail getSaleProductDetailByFpid(long fpid) {
        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("financial_product_id",fpid);
        return saleProductDetailMapper.selectOne(queryWrapper);
    }

    @Override
    public SaleProductDetail getSaleProductDetailBySaidAndFpid(long said, long fpid){
        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seckill_activity_id",said).eq("financial_product_id",fpid);
        return saleProductDetailMapper.selectOne(queryWrapper);
    }

    @Override
    public int addSaleProductDetail(SaleProductDetail saleProductDetail) {
        return saleProductDetailMapper.insert(saleProductDetail);
    }

    @Override
    public int deleteSaleProductDetail(long said,long fpid) {
        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seckill_activity_id",said).eq("financial_product_id",fpid);
        return saleProductDetailMapper.delete(queryWrapper);
    }

    @Override
    public int updateSaleProductDetail(SaleProductDetail saleProductDetail) {
        return saleProductDetailMapper.updateById(saleProductDetail);
    }
}
