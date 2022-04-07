package com.zaomengjia.auth.filter;

import com.zaomengjia.auth.constant.AuthorityGroup;
import com.zaomengjia.auth.exception.LoginErrorException;
import com.zaomengjia.common.dao.UserMapper;
import com.zaomengjia.common.pojo.User;
import io.netty.handler.codec.spdy.SpdyGoAwayFrame;
import io.netty.util.internal.StringUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 21:10
 */
@Component
public class UsernamePasswordManager implements ReactiveAuthenticationManager {

    private final UserMapper userMapper;

    public UsernamePasswordManager(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken authenticate = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authenticate.getPrincipal();
        String password = (String) authenticate.getCredentials();

        if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            return Mono.just(authentication);
        }

        User user = userMapper.getByUserNameAndPassword(username, password);
        user = new User();
        user.setUid(123);
        user.setType(0);

        if(user == null) {
            //找不到用户
            throw new LoginErrorException();
        }


        authenticate = new UsernamePasswordAuthenticationToken(username, password, AuthorityUtils.createAuthorityList(AuthorityGroup.ADMIN.raw));
        authenticate.setDetails(user);

        return Mono.just(authenticate);
    }
}
