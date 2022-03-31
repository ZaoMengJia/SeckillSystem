package com.zaomengjia.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 理财产品类，记录银行所有理财产品
 */
@Data
@TableName("financial_product")
public class FinancialProduct {
    @TableId(value = "fpid",type = IdType.AUTO)
    private long fpid;//银行所有理财产品的id
    @TableField("fname")
    private String fname;//理财产品名
    @TableField("price")
    private int price;//理财产品价格
}
