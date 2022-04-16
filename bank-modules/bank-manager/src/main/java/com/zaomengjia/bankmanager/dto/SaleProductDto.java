package com.zaomengjia.bankmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/16 00:12
 */
@Data
public class SaleProductDto {
    @NotEmpty
    private int quantity;

    @NotEmpty
    private int total;
}
