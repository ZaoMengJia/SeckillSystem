package com.zaomengjia.common.vo.bank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 20:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SaleProductVO extends FinancialProductVO {
    private Long quantity;
    private Long total;
}
