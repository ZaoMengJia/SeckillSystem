package com.zaomengjia.common.entity;


import lombok.Data;

import javax.persistence.*;


/**
 * 秒杀活动类，记录秒杀活动的id，名称，以及秒杀活动的商品明细
 * 商品明细由Detail类建表建立活动与对应理财产品
 */
@Data
@Entity(name = "seckill_activity")
public class SeckillActivity {

    /**
     * 秒杀活动id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "said")
    private long said;

    /**
     * 秒杀活动名称
     */
    @Column(name = "sname")
    private String sname;
}
