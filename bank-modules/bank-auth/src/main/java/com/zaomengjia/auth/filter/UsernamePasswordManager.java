package com.zaomengjia.auth.filter;

import com.zaomengjia.common.dao.UserMapper;
import com.zaomengjia.common.pojo.User;
import io.netty.handler.codec.spdy.SpdyGoAwayFrame;
import io.netty.util.internal.StringUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 21:10
 */
@Component
public class UsernamePasswordManager implements ReactiveAuthenticationManager {

    private UserMapper userMapper;

    public UsernamePasswordManager(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) usernamePasswordAuthenticationToken.getPrincipal();
        String password = (String) usernamePasswordAuthenticationToken.getCredentials();

        if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            return Mono.just(authentication);
        }

        User user = null;
        if(user == null) {
            //找不到用户
            throw new RuntimeException("");
        }



        return null;
    }
}
