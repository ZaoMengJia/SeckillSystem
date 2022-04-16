package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.FinancialProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FinancialProductMapper extends JpaRepository<FinancialProduct, String> {
    FinancialProduct findByName(String name);

    FinancialProduct findByPrice(int price);

    Page<FinancialProduct> findByNameContaining(String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update financial_product f set f.deleted = 1 where f.id = ?1")
    void deleteById(String id);
}
