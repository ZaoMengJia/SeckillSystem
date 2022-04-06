package com.zaomengjia.common.dao;

import com.zaomengjia.common.pojo.FinancialProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialProductMapper extends JpaRepository<FinancialProduct, Long> {
    FinancialProduct getByFname(String fname);

    FinancialProduct getByPrice(int price);

    Page<FinancialProduct> getByFnameLike(String keyword, Pageable pageable);
}
