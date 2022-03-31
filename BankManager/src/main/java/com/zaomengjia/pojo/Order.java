package com.zaomengjia.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 订单类，用户抢购秒杀商品时产生订单一人只能抢购一份商品
 */
@Data
@TableName("order")
public class Order {
    @TableId(value = "oid",type = IdType.AUTO)
    private long oid;//订单号
    @TableField("user_name")
    private String userName;//购买订单用户
    @TableField("financial_product_name")
    private String financialProductName;//购买的理财产品名
    @TableField("seckill_activity")
    private long said;//订单来自的秒杀活动
}
