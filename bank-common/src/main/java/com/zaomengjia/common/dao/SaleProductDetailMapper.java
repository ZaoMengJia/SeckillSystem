package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.SaleProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SaleProductDetailMapper extends JpaRepository<SaleProductDetail, String> {

    SaleProductDetail findByFinancialProductIdAndSeckillActivityId(String finalProductId, String seckillActivityId);

    SaleProductDetail findByFinancialProductId(String finalProductId);

    List<SaleProductDetail> findBySeckillActivityId(String seckillActivityId);

    void deleteBySeckillActivityIdAndFinancialProductId(String seckillActivityId, String finalProductId);

    Page<SaleProductDetail> findBySeckillActivityIdContaining(String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update com.zaomengjia.common.entity.SaleProductDetail set quantity = :quantity where financialProductId = :financialProductId and seckillActivityId = :seckillActivityId")
    int updateQuantity(@Param("financialProductId") String financialProductId, @Param("seckillActivityId") String seckillActivityId, @Param("quantity") long quantity);
}
