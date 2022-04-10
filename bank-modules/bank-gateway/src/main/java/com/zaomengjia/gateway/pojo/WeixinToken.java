package com.zaomengjia.gateway.pojo;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 12:03
 */
public class WeixinToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final String code;
    private final String openid;

    public WeixinToken(String code, String openid) {
        super(null);
        this.code = code;
        this.openid = openid;
        super.setAuthenticated(false);
    }

    public WeixinToken(String code, String openid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.code = code;
        this.openid = openid;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return openid;
    }

    @Override
    public Object getPrincipal() {
        return code;
    }
}
