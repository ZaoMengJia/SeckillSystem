package com.zaomengjia.order.service.impl;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.dao.WeixinUserMapper;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import com.zaomengjia.order.dto.UserInfoDto;
import com.zaomengjia.order.service.UserService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 22:37
 */
@Service
public class UserServiceImpl implements UserService {

    private final WeixinUserMapper weixinUserMapper;

    public UserServiceImpl(WeixinUserMapper weixinUserMapper) {
        this.weixinUserMapper = weixinUserMapper;
    }

    @Override
    public void updateUserInfo(UserInfoDto userInfoDto, String userId) {
        String openid = userInfoDto.getOpenid();
        WeixinUser user = Optional.of(weixinUserMapper.findByOpenid(openid)).orElseThrow(() -> new AppException(ResultCode.NO_SUCH_USER));
        user.setId(userId);
        user.setNickname(userInfoDto.getNickname());
        user.setGender(userInfoDto.getGender());
        user.setAvatarUrl(userInfoDto.getAvatarUrl());
        user.setIdCard(userInfoDto.getIdCard());
        user.setRealName(userInfoDto.getRealName());
        weixinUserMapper.save(user);
    }

    @Override
    public WeixinUserVO getUserInfo(String userId) {
        WeixinUser user = weixinUserMapper.findById(userId).orElseThrow(() -> new AppException(ResultCode.NO_SUCH_USER));
        WeixinUserVO vo = new WeixinUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public Pair<Boolean, Boolean> getUserState(String userId) {
        boolean isRegister;
        boolean isAudited;

        WeixinUser user = weixinUserMapper.findById(userId).orElse(null);
        if(user == null) {
            return Pair.of(false, false);
        }

        isRegister = !StringUtil.isNullOrEmpty(user.getRealName() + user.getIdCard());
        isAudited = user.getAudit() != 0;
        return Pair.of(isRegister, isAudited);
    }


}
