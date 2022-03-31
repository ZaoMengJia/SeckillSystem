package com.zaomengjia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaomengjia.dao.SaleProductDetailMapper;
import com.zaomengjia.pojo.Order;
import com.zaomengjia.pojo.SaleProductDetail;
import com.zaomengjia.pojo.SeckillActivity;
import com.zaomengjia.service.SaleProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaleProductServiceImpl implements SaleProductDetailService {

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
        return null;
    }

    @Override
    public SeckillActivity getSaleProductDetailBySaid() {
        return null;
    }

    @Override
    public SeckillActivity getSaleProductDetailByFpid() {
        return null;
    }

    @Override
    public int addSaleProductDetail() {
        return 0;
    }

    @Override
    public int deleteSaleProductDetail() {
        return 0;
    }

    @Override
    public int updateSaleProductDetail() {
        return 0;
    }
}
