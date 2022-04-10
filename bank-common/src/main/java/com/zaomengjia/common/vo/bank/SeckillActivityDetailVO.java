package com.zaomengjia.common.vo.bank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 20:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SeckillActivityDetailVO extends SeckillActivityVO {
    private List<SaleProductVO> productList;
}
