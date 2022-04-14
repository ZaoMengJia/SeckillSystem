package com.zaomengjia.common.dao;

import com.zaomengjia.common.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 23:52
 */
public interface AdminUserMapper extends JpaRepository<AdminUser, String> {
    AdminUser findByUsername(String username);

    Page<AdminUser> searchByUsernameContaining(String keyword, Pageable pageable);
}
