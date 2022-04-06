package com.zaomengjia.common.dao;


import com.zaomengjia.common.pojo.SeckillActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeckillActivityMapper extends JpaRepository<SeckillActivity, Long> {

    SeckillActivity getBySname(String sname);

    Page<SeckillActivity> getBySnameLike(String keyword, Pageable pageable);
}
