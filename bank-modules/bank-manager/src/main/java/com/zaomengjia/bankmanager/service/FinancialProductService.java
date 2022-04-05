package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.pojo.FinancialProduct;

import java.util.Map;

public interface FinancialProductService {
    Boolean financialProductExist(String fname);

    Map<String, Object> getFinancialProduct(int pageIndex, int pageSize);

    Map<String, Object> searchProduct(String keyword, int pageIndex, int pageSize);

    FinancialProduct getFinancialProductById(long fpid);

    FinancialProduct getFinancialProductByPrice(int price);

    FinancialProduct getFinancialProductByName(String fname);

    int addFinancialProduct(FinancialProduct financialProduct);

    int deleteFinancialProduct(long fpid);

    int updateFinancialProduct(FinancialProduct financialProduct);
}
