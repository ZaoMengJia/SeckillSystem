package com.zaomengjia.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaomengjia.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
