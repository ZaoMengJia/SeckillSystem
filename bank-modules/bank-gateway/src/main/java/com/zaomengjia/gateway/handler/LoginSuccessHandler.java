package com.zaomengjia.gateway.handler;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.entity.AdminUser;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.gateway.constant.AuthorityGroup;
import com.zaomengjia.gateway.utils.JwtUtils;
import com.zaomengjia.gateway.utils.ResponseWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 11:18
 */
public class LoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public LoginSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        Object details = authentication.getDetails();
        String jwt;
        String userId;
        JSONObject json = new JSONObject();
        if(details instanceof WeixinUser) {
            WeixinUser weixinUser = (WeixinUser) details;
            userId = weixinUser.getId();
            jwt = jwtUtils.getWeixinJwt(Collections.singletonList(AuthorityGroup.USER), userId, weixinUser.getAudit());
            json.put("openId", weixinUser.getOpenid());
        }
        else {
            AdminUser user = (AdminUser) authentication.getDetails();
            AuthorityGroup authorityGroup = AuthorityGroup.ADMIN;
            userId = user.getId();
            jwt = jwtUtils.getAdminJwt(Collections.singletonList(authorityGroup), userId);
        }


        json.put("token", "Bearer " + jwt);
        json.put("userId", userId);
        return ResponseWriter.writeResponse(webFilterExchange.getExchange(), ResultUtils.success(json));

    }
}
