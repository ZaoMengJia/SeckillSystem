package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.pojo.SeckillActivity;

import java.util.Map;

public interface SaleProductDetailService {
    Boolean saleProductDetailExist(long said,long fpid);

    Map<String,Object> getSaleProductDetail(int pageIndex,int pageSize);

    SeckillActivity getSaleProductDetailBySaid();

    SeckillActivity getSaleProductDetailByFpid();

    int addSaleProductDetail();
    int deleteSaleProductDetail();
    int updateSaleProductDetail();
}
