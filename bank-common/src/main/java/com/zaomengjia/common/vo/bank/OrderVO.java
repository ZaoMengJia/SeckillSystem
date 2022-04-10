package com.zaomengjia.common.vo.bank;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 01:19
 */
@Data
@Accessors(chain = true)
public class OrderVO {
    private String id;
    private FinancialProductVO financialProduct;
    private SeckillActivityVO seckillActivity;
    private Date createTime;
    private Long quantity;
}
