package com.zaomengjia.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 秒杀活动的商品明细类，包括商品名，商品数量,属于哪一个秒杀活动
 */
@Data
@TableName("sale_product_detail")
public class SaleProductDetail {
    @TableField("seckill_activity_id")
    private long said;//隶属于哪一个秒杀活动
    @TableField("financial_product_id")
    private long fpid;//理财的产品
    @TableField("quantity")
    private int quantity;//活动抢购数量
}
