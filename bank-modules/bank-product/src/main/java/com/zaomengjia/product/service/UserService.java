package com.zaomengjia.product.service;

import com.zaomengjia.common.vo.user.WeixinUserVO;
import com.zaomengjia.product.dto.UserInfoDto;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:37
 */
public interface UserService {
    void updateUserInfo(UserInfoDto userInfoDto);

    WeixinUserVO getUserInfo(String userId);
}
