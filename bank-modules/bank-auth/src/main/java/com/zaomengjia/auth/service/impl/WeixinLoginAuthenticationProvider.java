package com.zaomengjia.auth.service.impl;

import com.zaomengjia.auth.pojo.WeixinAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 12:02
 */
@Component
public class WeixinLoginAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = authentication.getName();


        //Todo 获取openid
        String openid = "";
        if(false) {
            throw new AuthenticationServiceException("12");
        }
        return new WeixinAuthenticationToken(code, openid, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeixinAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
