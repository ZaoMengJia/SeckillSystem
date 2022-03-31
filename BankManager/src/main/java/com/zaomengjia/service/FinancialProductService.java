package com.zaomengjia.service;

import com.zaomengjia.pojo.FinancialProduct;

import java.util.Map;

public interface FinancialProductService {
    Boolean financialProductExist(String fname);

    Map<String, Object> getFinancialProduct(int pageIndex, int pageSize);

    FinancialProduct getFinancialProductById(long fpid);

    FinancialProduct getFinancialProductByPrice(int price);

    FinancialProduct getFinancialProductByName(String fname);

    int addFinancialProduct(FinancialProduct financialProduct);

    int deleteFinancialProduct(String fpid);

    int updateFinancialProduct(FinancialProduct financialProduct);
}
