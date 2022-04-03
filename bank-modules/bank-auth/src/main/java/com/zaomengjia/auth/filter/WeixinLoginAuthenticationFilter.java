package com.zaomengjia.auth.filter;

import com.zaomengjia.auth.pojo.WeixinAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 02:09
 */
public class WeixinLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final static RequestMatcher REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    private final static String ACCEPT_METHOD = "POST";

    public WeixinLoginAuthenticationFilter() {
        super(REQUEST_MATCHER);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //Todo: 从parameters里获取code

        if(!ACCEPT_METHOD.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String code = request.getParameter("code");

        WeixinAuthenticationToken weixinAuthenticationToken = new WeixinAuthenticationToken(code, null);
        weixinAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));

        return getAuthenticationManager().authenticate(weixinAuthenticationToken);
    }
}
