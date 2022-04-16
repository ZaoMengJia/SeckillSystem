package com.zaomengjia.bankmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 20:42
 */
@Data
public class FinancialProductDto {
    @NotEmpty
    private String name;

    @NotNull
    private Double price;
}
