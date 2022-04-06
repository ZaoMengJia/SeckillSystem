package com.zaomengjia.auth.config;

import com.zaomengjia.auth.filter.JwtFilter;
import com.zaomengjia.auth.filter.UsernamePasswordManager;
import com.zaomengjia.auth.pojo.JwtToken;
import com.zaomengjia.auth.pojo.WeixinToken;
import com.zaomengjia.auth.filter.WeixinLoginManager;
import com.zaomengjia.common.utils.MD5Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

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


    public AuthenticationWebFilter usernamePasswordFilter(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/web"));
        authenticationWebFilter.setServerAuthenticationConverter(exchange -> exchange.getFormData().map(data -> {
            String username = data.getFirst("username");
            String password = MD5Utils.fromMD5(Objects.requireNonNull(data.getFirst("password")));
            return new UsernamePasswordAuthenticationToken(username, password);
        }));
        authenticationWebFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> responseStatusExceptionHandler.handle(webFilterExchange.getExchange(), exception));
        return authenticationWebFilter;
    }


    public AuthenticationWebFilter weixinFilter(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/weixin"));
        authenticationWebFilter.setServerAuthenticationConverter(exchange -> exchange.getFormData().map(data -> {
            String code = data.getFirst("code");
            return new WeixinToken(code, null);
        }));
        authenticationWebFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> responseStatusExceptionHandler.handle(webFilterExchange.getExchange(), exception));
        return authenticationWebFilter;
    }

//    public AuthenticationWebFilter jwtFilter(ReactiveAuthenticationManager reactiveAuthenticationManager) {
//        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
//        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
//        authenticationWebFilter.setServerAuthenticationConverter(exchange ->
//                Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
//                        .switchIfEmpty(Mono.error(new RuntimeException("空数据")))
//                        .map(JwtToken::new)
//        );
//        authenticationWebFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> responseStatusExceptionHandler.handle(webFilterExchange.getExchange(), exception));
//        return authenticationWebFilter;
//    }

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
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager reactiveAuthenticationManager, JwtFilter jwtFilter) {
        http
                .authenticationManager(reactiveAuthenticationManager)
                .exceptionHandling()
                .accessDeniedHandler((exchange, denied) -> responseStatusExceptionHandler.handle(exchange, denied))
                .authenticationEntryPoint((exchange, ex) -> responseStatusExceptionHandler.handle(exchange, ex))
                .and()
                .authorizeExchange()
                .pathMatchers("/auth/weixin/**").permitAll()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/item/**").hasRole("USER")
                .anyExchange().authenticated()
                .and()
                .addFilterAt(weixinFilter(reactiveAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(usernamePasswordFilter(reactiveAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable().exceptionHandling();
        return http.build();
    }
}
