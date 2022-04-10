package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.SaleProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleProductDetailMapper extends JpaRepository<SaleProductDetail, String> {

    SaleProductDetail findByFinancialProductIdAndSeckillActivityId(String finalProductId, String seckillActivityId);

    SaleProductDetail findByFinancialProductId(String finalProductId);

    List<SaleProductDetail> findBySeckillActivityId(String seckillActivityId);

    void deleteBySeckillActivityIdAndFinancialProductId(String seckillActivityId, String finalProductId);

    Page<SaleProductDetail> findBySeckillActivityIdLike(String keyword, Pageable pageable);
}
