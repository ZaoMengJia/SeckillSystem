package com.zaomengjia.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


/**
 * 秒杀活动类，记录秒杀活动的id，名称，以及秒杀活动的商品明细
 * 商品明细由Detail类建表建立活动与对应理财产品
 */
@Data
@TableName("seckill_activity")
public class SeckillActivity {
    @TableId(value = "said",type = IdType.AUTO)
    private long said;//秒杀活动id
    @TableField("sname")
    private String sname;//秒杀活动名称
}
