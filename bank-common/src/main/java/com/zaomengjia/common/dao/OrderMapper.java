package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends JpaRepository<Order, Long> {
    Order findByFinancialProductName(String finalProductName);

    Order findByUserName(String username);

    Order findByOid(long oid);

    Page<Order> findByUserNameLike(String keyword, Pageable pageable);
}
