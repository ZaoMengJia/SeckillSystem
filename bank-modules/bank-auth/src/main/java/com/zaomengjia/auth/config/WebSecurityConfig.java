package com.zaomengjia.auth.config;

import com.zaomengjia.auth.filter.WeixinLoginAuthenticationFilter;
import com.zaomengjia.auth.service.impl.WeixinLoginAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 11:48
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WeixinLoginAuthenticationProvider weixinLoginAuthenticationProvider;


    public WebSecurityConfig(WeixinLoginAuthenticationProvider weixinLoginAuthenticationProvider) {
        this.weixinLoginAuthenticationProvider = weixinLoginAuthenticationProvider;
    }

    WeixinLoginAuthenticationFilter weixinLoginAuthenticationFilter() throws Exception {
        WeixinLoginAuthenticationFilter weixinLoginAuthenticationFilter = new WeixinLoginAuthenticationFilter();

        weixinLoginAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {

        });
        weixinLoginAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {

        });
        weixinLoginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        weixinLoginAuthenticationFilter.setFilterProcessesUrl("/weixin/login");
        return weixinLoginAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable().exceptionHandling();

        http.addFilterAt(weixinLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //微信openid登录
        auth.authenticationProvider(weixinLoginAuthenticationProvider);
    }
}
