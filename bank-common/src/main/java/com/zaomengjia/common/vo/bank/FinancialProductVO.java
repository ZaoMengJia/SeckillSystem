package com.zaomengjia.common.vo.bank;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 18:02
 */
@Data
@Accessors(chain = true)
public class FinancialProductVO {
    private String id;
    private String name;
    private Double price;
}
