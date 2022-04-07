package com.zaomengjia.bankmanager.dto;

import lombok.Data;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 01:43
 */
@Data
public class WeixinUserDto {
    private String nickname;
    private String realName;
    private String idCard;
    private Integer gender;
}
