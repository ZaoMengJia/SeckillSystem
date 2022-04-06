package com.zaomengjia.common.pojo;

import lombok.Data;

import javax.persistence.*;


/**
 * 秒杀活动的商品明细类，包括商品名，商品数量,属于哪一个秒杀活动
 */
@Data
@Entity(name = "sale_product_detail")
public class SaleProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 隶属于哪一个秒杀活动
     */
    @Column(name = "seckill_activity_id")
    private long said;

    /**
     * 理财的产品
     */
    @Column(name = "financial_product_id")
    private long fpid;

    /**
     * 活动抢购数量
     */
    @Column(name = "quantity")
    private int quantity;


}
