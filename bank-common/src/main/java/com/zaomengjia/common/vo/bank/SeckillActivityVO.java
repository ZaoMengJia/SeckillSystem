package com.zaomengjia.common.vo.bank;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 18:02
 */
@Data
@Accessors(chain = true)
public class SeckillActivityVO {
    private String id;
    private String name;
    private String image;
    private String detail;
    private Date beginTime;
    private Date endTime;
}
