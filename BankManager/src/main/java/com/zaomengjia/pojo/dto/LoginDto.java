package com.zaomengjia.pojo.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class LoginDto implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String loginName;
    @NotBlank(message = "密码不能为空")
    private String password;
}
