package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.entity.SaleProductDetail;

import java.util.Map;

public interface SaleProductDetailService {
    Boolean saleProductDetailExist(long said,long fpid);

    Map<String,Object> getSaleProductDetail(int pageIndex,int pageSize);

    Map<String, Object> searchDetail(String keyword, int pageIndex, int pageSize);

    SaleProductDetail getSaleProductDetailBySaid(long said);

    SaleProductDetail getSaleProductDetailByFpid(long fpid);

    SaleProductDetail getSaleProductDetailBySaidAndFpid(long said,long fpid);

    void addSaleProductDetail(SaleProductDetail saleProductDetail);
    void deleteSaleProductDetail(long said, long fpid);
    void updateSaleProductDetail(SaleProductDetail saleProductDetail);
}
