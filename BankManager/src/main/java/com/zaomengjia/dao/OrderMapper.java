package com.zaomengjia.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaomengjia.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
