package com.zaomengjia.order.service;

import com.zaomengjia.common.vo.user.WeixinUserVO;
import com.zaomengjia.order.dto.UserInfoDto;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:37
 */
public interface UserService {
    void updateUserInfo(UserInfoDto userInfoDto);

    WeixinUserVO getUserInfo(String userId);
}
