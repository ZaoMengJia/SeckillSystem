package com.zaomengjia.bankmanager.vo;

import com.zaomengjia.common.constant.OrderStatus;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 21:11
 */
@Data
@Accessors(chain = true)
public class OrderVO {
    private String id;
    private WeixinUserVO weixinUser;
    private FinancialProductVO financialProduct;
    private SeckillActivityVO seckillActivity;
    private Date createTime;
    private Long quantity;
    private OrderStatus status;
}
