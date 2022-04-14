package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.FinancialProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialProductMapper extends JpaRepository<FinancialProduct, String> {
    FinancialProduct findByName(String fname);

    FinancialProduct findByPrice(int price);

    Page<FinancialProduct> findByNameContaining(String keyword, Pageable pageable);
}
