package com.zaomengjia.bankmanager.vo;

import com.zaomengjia.common.vo.user.WeixinUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/17 00:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeixinUserDetailVO extends WeixinUserVO {
    private Boolean hasJob;
    private Boolean isDiscredit;
    private Integer overdueRecord;
    private int audit;
}
