package com.zaomengjia.bankmanager.vo;

import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.SaleProductVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 23:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SeckillActivityWithProductListVO extends SeckillActivityVO {
    private List<SaleProductVO> productList;
}
