package com.zaomengjia.bankmanager.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 00:40
 */
@Data
public class AdminUserDto {
    @NotNull
    private String username;

    private String password;

    @NotNull
    private String avatarUrl;
}
