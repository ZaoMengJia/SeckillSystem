package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper extends JpaRepository<Order, String> {
    Order findByFinancialProductId(String finalProductId);

    Order findByUserId(String userId);

    Page<Order> findByUserId(String userId, Pageable pageable);

    Page<Order> findByUserIdLike(String keyword, Pageable pageable);

    List<Order> findByUserIdAndFinancialProductIdAndSeckillActivityId(String userId, String financialProductId, String seckillActivityId);
}
