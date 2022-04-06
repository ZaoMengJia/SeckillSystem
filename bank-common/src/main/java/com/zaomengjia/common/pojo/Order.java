package com.zaomengjia.common.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * 订单类，用户抢购秒杀商品时产生订单一人只能抢购一份商品
 */
@Data
@Entity(name = "user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")

    /**
     * 订单号
     */
    private long oid;

    /**
     * 购买订单用户
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 购买的理财产品名
     */
    @Column(name = "financial_product_name")
    private String financialProductName;

    /**
     * 订单来自的秒杀活动
     */
    @Column(name = "seckill_activity")
    private long said;
}
