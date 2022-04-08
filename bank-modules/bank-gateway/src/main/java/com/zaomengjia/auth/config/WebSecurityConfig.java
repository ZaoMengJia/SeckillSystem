package com.zaomengjia.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.auth.constant.AuthorityGroup;
import com.zaomengjia.auth.filter.JwtFilter;
import com.zaomengjia.auth.filter.UsernamePasswordManager;
import com.zaomengjia.auth.pojo.WeixinToken;
import com.zaomengjia.auth.filter.WeixinLoginManager;
import com.zaomengjia.auth.utils.JwtUtils;
import com.zaomengjia.auth.utils.ResponseWriter;
import com.zaomengjia.common.entity.User;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.utils.ResultUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 11:48
 */
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {


    @Resource
    private WebFluxResponseStatusExceptionHandler responseStatusExceptionHandler;


    public AuthenticationWebFilter usernamePasswordFilter(ReactiveAuthenticationManager reactiveAuthenticationManager, JwtUtils jwtUtils) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/web"));
        authenticationWebFilter.setServerAuthenticationConverter(exchange ->  exchange.getFormData().map(data -> {
            if(exchange.getRequest().getMethod() != HttpMethod.POST) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            String username = data.getFirst("username");
            String password = data.getFirst("password");

            if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return new UsernamePasswordAuthenticationToken(username, password);
        }));
        authenticationWebFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> responseStatusExceptionHandler.handle(webFilterExchange.getExchange(), exception));
        authenticationWebFilter.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> onAuthenticationSuccess(webFilterExchange, authentication, jwtUtils));
        return authenticationWebFilter;
    }


    public AuthenticationWebFilter weixinFilter(ReactiveAuthenticationManager reactiveAuthenticationManager, JwtUtils jwtUtils) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/weixin"));
        authenticationWebFilter.setServerAuthenticationConverter(exchange -> exchange.getFormData().map(data -> {
            if(exchange.getRequest().getMethod() != HttpMethod.POST) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            String code = data.getFirst("code");
            if(StringUtil.isNullOrEmpty(code)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return new WeixinToken(code, null);
        }));
        authenticationWebFilter.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> onAuthenticationSuccess(webFilterExchange, authentication, jwtUtils));
        authenticationWebFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> responseStatusExceptionHandler.handle(webFilterExchange.getExchange(), exception));
        return authenticationWebFilter;
    }

    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication, JwtUtils jwtUtils) {
        Object details = authentication.getDetails();
        String jwt;
        if(details instanceof WeixinUser) {
            WeixinUser weixinUser = (WeixinUser) details;
            String userId = weixinUser.getId();
            jwt = jwtUtils.getJwt(Collections.singletonList(AuthorityGroup.USER), userId);
        }
        else {
            User user = (User) authentication.getDetails();

            //下面这个以后有可能用到
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isUser = authorities.contains(AuthorityGroup.USER.getAuthority());
            boolean isAdmin = authorities.contains(AuthorityGroup.ADMIN.getAuthority());

            AuthorityGroup authorityGroup = user.getType() == 0 ? AuthorityGroup.USER : AuthorityGroup.ADMIN;
            jwt = jwtUtils.getJwt(Collections.singletonList(authorityGroup), user.getUid());
        }

        JSONObject json = new JSONObject();
        json.put("token", "Bearer " + jwt);
        return ResponseWriter.writeResponse(webFilterExchange.getExchange(), ResultUtils.success(json));
    }



    @Bean
    @Primary
    public ReactiveAuthenticationManager reactiveAuthenticationManager(WeixinLoginManager weixinLoginManager, UsernamePasswordManager usernamePasswordManager, JwtFilter jwtFilter) {
        return authentication -> {
            if(authentication instanceof WeixinToken) {
                return weixinLoginManager.authenticate(authentication);
            }
            else if(authentication instanceof UsernamePasswordAuthenticationToken) {
                return usernamePasswordManager.authenticate(authentication);
            }

            return Mono.just(authentication);
        };
    }


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager reactiveAuthenticationManager, JwtFilter jwtFilter, JwtUtils jwtUtils) {
        http
                .authenticationManager(reactiveAuthenticationManager)
                .exceptionHandling()
                .accessDeniedHandler((exchange, denied) -> responseStatusExceptionHandler.handle(exchange, denied))
                .authenticationEntryPoint((exchange, ex) -> responseStatusExceptionHandler.handle(exchange, ex))

                .and()
                .authorizeExchange()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/web/**").permitAll()
//                    .hasRole(AuthorityGroup.ADMIN.raw)
                .pathMatchers("/item/**")
                .permitAll()
//                    .hasRole(AuthorityGroup.USER.raw)

                //OpenApi
                .pathMatchers(
                        "/swagger-ui.html",
                        "/v3/api-docs-gateway",
                        "/swagger-config.json",
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/webjars/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().permitAll()

                .and()
                .addFilterAt(weixinFilter(reactiveAuthenticationManager, jwtUtils), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(usernamePasswordFilter(reactiveAuthenticationManager, jwtUtils), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable().exceptionHandling()
                .and()
                .cors().configurationSource(urlBasedCorsConfigurationSource());
        return http.build();
    }

    private UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.applyPermitDefaultValues();
        // corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource ccs = new UrlBasedCorsConfigurationSource();
        ccs.registerCorsConfiguration("/**", corsConfiguration);
        return ccs;
    }
}