package com.zaomengjia.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaomengjia.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM `user` WHERE `user_name` = #{username} AND `password` = #{password}")
    User selectByUserNameAndPassword(String username, String password);
}
