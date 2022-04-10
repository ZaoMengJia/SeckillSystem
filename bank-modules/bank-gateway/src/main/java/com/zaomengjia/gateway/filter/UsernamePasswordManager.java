package com.zaomengjia.gateway.filter;

import com.zaomengjia.common.dao.AdminUserMapper;
import com.zaomengjia.common.entity.AdminUser;
import com.zaomengjia.gateway.constant.AuthorityGroup;
import com.zaomengjia.gateway.exception.LoginErrorException;
import com.zaomengjia.common.entity.User;
import io.netty.util.internal.StringUtil;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 21:10
 */
@Component
public class UsernamePasswordManager implements ReactiveAuthenticationManager {

    private final AdminUserMapper adminUserMapper;

    public UsernamePasswordManager(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken authenticate = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authenticate.getPrincipal();
        String password = (String) authenticate.getCredentials();

        if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            return Mono.just(authentication);
        }

        AdminUser admin = adminUserMapper.findByUsername(username);

        if(admin == null) {
            //找不到用户
            throw new LoginErrorException();
        }

        if(admin.getPassword().equals(password)) {
            authenticate = new UsernamePasswordAuthenticationToken(username, password, AuthorityUtils.createAuthorityList(AuthorityGroup.ADMIN.raw));
            authenticate.setDetails(admin);
        }

        return Mono.just(authenticate);
    }
}
