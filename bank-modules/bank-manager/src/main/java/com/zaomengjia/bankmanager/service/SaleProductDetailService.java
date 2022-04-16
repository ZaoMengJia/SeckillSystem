package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.entity.SaleProductDetail;

import java.util.List;
import java.util.Map;

public interface SaleProductDetailService {
    Boolean saleProductDetailExist(String seckillActivityId, String financialProductId);

    Map<String,Object> getSaleProductDetail(int pageIndex,int pageSize);

    Map<String, Object> searchDetail(String keyword, int pageIndex, int pageSize);

    List<SaleProductDetail> getSaleProductDetailBySaid(String said);

    SaleProductDetail getSaleProductDetailByFpid(String fpid);

    SaleProductDetail getSaleProductDetailBySaidAndFpid(String said,String fpid);

    void addSaleProductDetail(SaleProductDetail saleProductDetail);
    void deleteSaleProductDetail(String said, String fpid);
    void updateSaleProductDetail(SaleProductDetail saleProductDetail);
}
