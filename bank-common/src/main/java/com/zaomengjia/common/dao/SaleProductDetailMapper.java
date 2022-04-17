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

    @Query("update sale_product_detail s set s.deleted = 1 where s.seckillActivityId = ?1 and s.financialProductId = ?2")
    @Modifying
    @Transactional
    void deleteBySeckillActivityIdAndFinancialProductId(String seckillActivityId, String finalProductId);

    @Query("update sale_product_detail s set s.deleted = 1 where s.financialProductId = ?1")
    @Modifying
    @Transactional
    void deleteByFinancialProductId(String financialProductId);

    void deleteBySeckillActivityId(String seckillActivityId);

    Page<SaleProductDetail> findBySeckillActivityIdContaining(String keyword, Pageable pageable);
    @Transactional
    @Modifying
    @Query(value = "update com.zaomengjia.common.entity.SaleProductDetail set quantity = :quantity, total = :total where financialProductId = :financialProductId and seckillActivityId = :seckillActivityId")
    int updateQuantityAndTotal(@Param("financialProductId") String financialProductId, @Param("seckillActivityId") String seckillActivityId, @Param("quantity") long quantity, @Param("total") long total);

    @Transactional
    @Modifying
    @Query(value = "update com.zaomengjia.common.entity.SaleProductDetail set quantity = :quantity where financialProductId = :financialProductId and seckillActivityId = :seckillActivityId")
    int updateQuantity(@Param("financialProductId") String financialProductId, @Param("seckillActivityId") String seckillActivityId, @Param("quantity") long quantity);
}
