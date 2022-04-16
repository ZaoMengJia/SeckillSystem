package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.WeixinUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:10
 */
@Repository
public interface WeixinUserMapper extends JpaRepository<WeixinUser, String> {
    WeixinUser findByOpenid(String openid);

    Page<WeixinUser> searchByNicknameContaining(String keyword, Pageable pageable);

    Page<WeixinUser> searchByRealNameContaining(String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update com.zaomengjia.common.entity.WeixinUser set audit = :audit where id = :id")
    void setAudit(@Param("id") String id, @Param("audit") int audit);

}
