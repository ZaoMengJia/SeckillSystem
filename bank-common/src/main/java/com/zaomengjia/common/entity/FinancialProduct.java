package com.zaomengjia.common.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 理财产品类，记录银行所有理财产品
 */
@Data
@Entity(name = "financial_product")
public class FinancialProduct {

    /**
     * 银行所有理财产品的id
     */
    @Id
    @Column(name = "fpid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fpid;

    /**
     * 理财产品名
     */
    @Column(name = "fname")
    private String fname;

    /**
     * 理财产品价格
     */
    @Column(name = "price")
    private int price;
}
