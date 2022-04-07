package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.entity.FinancialProduct;

import java.util.Map;

public interface FinancialProductService {
    Boolean financialProductExist(String fname);

    Map<String, Object> getFinancialProduct(int pageIndex, int pageSize);

    Map<String, Object> searchProduct(String keyword, int pageIndex, int pageSize);

    FinancialProduct getFinancialProductById(long fpid);

    FinancialProduct getFinancialProductByPrice(int price);

    FinancialProduct getFinancialProductByName(String fname);

    void addFinancialProduct(FinancialProduct financialProduct);

    void deleteFinancialProduct(long fpid);

    void updateFinancialProduct(FinancialProduct financialProduct);
}
