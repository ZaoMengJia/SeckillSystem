package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.WeixinUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:10
 */
@Repository
public interface WeixinUserMapper extends JpaRepository<WeixinUser, String> {
    WeixinUser findByOpenid(String openid);

    Page<WeixinUser> searchByNicknameLike(String keyword, Pageable pageable);

    Page<WeixinUser> searchByRealNameLike(String keyword, Pageable pageable);



}
