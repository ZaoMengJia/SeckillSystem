package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.FinancialProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialProductMapper extends JpaRepository<FinancialProduct, String> {
    FinancialProduct getByName(String name);

    FinancialProduct getByPrice(int price);

    Page<FinancialProduct> getByNameLike(String keyword, Pageable pageable);
}
