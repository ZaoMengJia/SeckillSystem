package com.zaomengjia.common.dao;

import com.zaomengjia.common.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<User, Long> {

    User getByTypeAndUserName(int type, String username);

    Page<User> getByType(int type, Pageable pageable);

    User getByUserName(String username);

    User getByTypeAndUid(int type, long uid);

    User getByUserNameAndPassword(String username, String password);

    User getByWxOpenid(String wxOpenid);

}
