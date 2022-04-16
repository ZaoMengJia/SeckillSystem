package com.zaomengjia.bankmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 20:30
 */
@Data
public class SeckillActivityDto {
    @NotEmpty
    private String name;

    @NotNull
    private Date beginTime;

    @NotNull
    private Date endTime;

    @NotEmpty
    private String detail;
    private String image;
}
