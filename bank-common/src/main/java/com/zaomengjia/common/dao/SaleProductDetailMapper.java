package com.zaomengjia.common.dao;

import com.zaomengjia.common.pojo.SaleProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductDetailMapper extends JpaRepository<SaleProductDetail, Long> {

    SaleProductDetail findByFpidAndSaid(long said, long fpid);

    SaleProductDetail findByFpid(long fpid);

    SaleProductDetail findBySaid(long said);

    void deleteBySaidAndFpid(long said, long fpid);

    Page<SaleProductDetail> findBySaidLike(String keyword, Pageable pageable);
}
