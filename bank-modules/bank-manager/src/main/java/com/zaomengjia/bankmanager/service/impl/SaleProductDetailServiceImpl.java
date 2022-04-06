package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.pojo.SaleProductDetail;
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
        return saleProductDetailMapper.findByFpidAndSaid(said, fpid) !=null;
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
        Page<SaleProductDetail> result = saleProductDetailMapper.findBySaidLike(keyword, PageRequest.of(pageIndex, pageSize));
        map.put("records", result.toList());
        map.put("total", result.getTotalElements());
        return map;
    }

    @Override
    public SaleProductDetail getSaleProductDetailBySaid(long said) {
        return saleProductDetailMapper.findBySaid(said);
    }

    @Override
    public SaleProductDetail getSaleProductDetailByFpid(long fpid) {
        return saleProductDetailMapper.findByFpid(fpid);
    }

    @Override
    public SaleProductDetail getSaleProductDetailBySaidAndFpid(long said, long fpid){
        return saleProductDetailMapper.findByFpidAndSaid(fpid, said);
    }

    @Override
    public void addSaleProductDetail(SaleProductDetail saleProductDetail) {
        saleProductDetailMapper.save(saleProductDetail);
    }

    @Override
    public void deleteSaleProductDetail(long said, long fpid) {
        saleProductDetailMapper.deleteBySaidAndFpid(said, fpid);
    }

    @Override
    public void updateSaleProductDetail(SaleProductDetail saleProductDetail) {
        saleProductDetailMapper.save(saleProductDetail);
    }
}
