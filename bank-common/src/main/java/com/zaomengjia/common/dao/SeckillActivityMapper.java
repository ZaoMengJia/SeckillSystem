package com.zaomengjia.common.dao;


import com.zaomengjia.common.entity.SeckillActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeckillActivityMapper extends JpaRepository<SeckillActivity, String> {

    SeckillActivity findByName(String name);

    Page<SeckillActivity> findByNameContaining(String keyword, Pageable pageable);
}
