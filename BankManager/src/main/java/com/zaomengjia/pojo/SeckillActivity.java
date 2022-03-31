package com.zaomengjia.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


/**
 * 秒杀活动类，记录秒杀活动的id，名称，以及秒杀活动的商品明细
 */
@Data
@TableName("seckill_activity")
public class SeckillActivity {
    @TableId(value = "said",type = IdType.AUTO)
    private long said;//秒杀活动id
    @TableField("sname")
    private String sname;//秒杀活动名称

    private List<SaleProductDetail> details;//秒杀活动的所有售卖理财产品
}
