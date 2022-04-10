package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.bankmanager.service.SaleProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaleProductDetailServiceImpl implements SaleProductDetailService {

    @Autowired
    SaleProductDetailMapper saleProductDetailMapper;

    @Override
    public Boolean saleProductDetailExist(long said,long fpid) {
        return saleProductDetailMapper.findByFinancialProductIdAndSeckillActivityId(said, fpid) !=null;
    }

    @Override
    public Map<String, Object> getSaleProductDetail(int pageIndex,int pageSize) {
        Map<String, Object> map = new HashMap<>(5);
        Page<SaleProductDetail> page = saleProductDetailMapper.findAll(PageRequest.of(pageIndex, pageSize));
        map.put("records", page.toList());
        map.put("total", page.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> searchDetail(String keyword, int pageIndex, int pageSize) {
        //Todo: 这里是不是有问题
//        QueryWrapper<SaleProductDetail> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("seckill_activity_id", keyword);
//        Page<SaleProductDetail> page = new Page<>(pageIndex, pageSize);


        Map<String, Object> map = new HashMap<>(5);
        Page<SaleProductDetail> result = saleProductDetailMapper.findBySeckillActivityIdLike(keyword, PageRequest.of(pageIndex, pageSize));
        map.put("records", result.toList());
        map.put("total", result.getTotalElements());
        return map;
    }

    @Override
    public SaleProductDetail getSaleProductDetailBySaid(long said) {
        return saleProductDetailMapper.findBySeckillActivityId(said);
    }

    @Override
    public SaleProductDetail getSaleProductDetailByFpid(long fpid) {
        return saleProductDetailMapper.findByFinancialProductId(fpid);
    }

    @Override
    public SaleProductDetail getSaleProductDetailBySaidAndFpid(long said, long fpid){
        return saleProductDetailMapper.findByFinancialProductIdAndSeckillActivityId(fpid, said);
    }

    @Override
    public void addSaleProductDetail(SaleProductDetail saleProductDetail) {
        saleProductDetailMapper.save(saleProductDetail);
    }

    @Override
    public void deleteSaleProductDetail(long said, long fpid) {
        saleProductDetailMapper.deleteBySeckillActivityIdAndFinancialProductId(said, fpid);
    }

    @Override
    public void updateSaleProductDetail(SaleProductDetail saleProductDetail) {
        saleProductDetailMapper.save(saleProductDetail);
    }
}
