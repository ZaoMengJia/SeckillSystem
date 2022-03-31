package com.zaomengjia.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaomengjia.common.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
