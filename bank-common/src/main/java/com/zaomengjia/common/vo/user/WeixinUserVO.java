package com.zaomengjia.common.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 23:41
 */
@Data
public class WeixinUserVO {
    private String id;
    private String openid;
    private String nickname;
    private String realName;
    private int gender;
    private String avatarUrl;
    private String idCard;
    private Date birthday;
}
