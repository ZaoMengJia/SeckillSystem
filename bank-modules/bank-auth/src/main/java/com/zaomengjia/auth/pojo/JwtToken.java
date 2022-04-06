package com.zaomengjia.auth.pojo;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/3 11:48
 */
public class JwtToken extends AbstractAuthenticationToken {

    private String jwt;

    public JwtToken(String jwt) {
        super(null);
        this.jwt = jwt;
    }

    public JwtToken(String jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public JwtToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
