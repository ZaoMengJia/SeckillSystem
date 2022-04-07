package com.zaomengjia.product.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:00
 */
@Data
public class UserInfoDto {
    @NotNull
    private String openid;

    @NotNull
    private String nickname;

    @NotNull
    private int gender;

    @NotNull
    private String avatarUrl;

    @NotNull
    private String idCard;

    @NotNull
    private Date birthday;

    @NotNull
    private String realName;
}
