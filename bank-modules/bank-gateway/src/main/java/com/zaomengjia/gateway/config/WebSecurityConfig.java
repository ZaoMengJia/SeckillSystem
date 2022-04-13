package com.zaomengjia.gateway.config;

import com.zaomengjia.gateway.filter.JwtFilter;
import com.zaomengjia.gateway.filter.SignatureFilter;
import com.zaomengjia.gateway.filter.UsernamePasswordManager;
import com.zaomengjia.gateway.filter.WeixinLoginManager;
import com.zaomengjia.gateway.handler.LoginSuccessHandler;
import com.zaomengjia.gateway.pojo.WeixinToken;
import com.zaomengjia.gateway.utils.JwtUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
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
        authenticationWebFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtUtils));
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
        authenticationWebFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtUtils));
        authenticationWebFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> responseStatusExceptionHandler.handle(webFilterExchange.getExchange(), exception));
        return authenticationWebFilter;
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
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager reactiveAuthenticationManager, JwtFilter jwtFilter, JwtUtils jwtUtils, SignatureFilter signatureFilter) {
        http
                .authenticationManager(reactiveAuthenticationManager)
                .exceptionHandling()

                //未授权的处理（交给失败控制器）
                .accessDeniedHandler((exchange, denied) -> responseStatusExceptionHandler.handle(exchange, denied))
                .authenticationEntryPoint((exchange, ex) -> responseStatusExceptionHandler.handle(exchange, ex))

                //配置路径权限
                .and()
                .authorizeExchange()
                //1. 认证服务，放行
                .pathMatchers("/auth/**").permitAll()

                //2. 管理员服务，需要ADMIN的角色
                .pathMatchers("/web/**").permitAll()
//                    .hasRole(AuthorityGroup.ADMIN.raw)

                //3. 用户订单服务，需要USER的角色
                .pathMatchers("/weixin/**")
                .permitAll()
//                    .hasRole(AuthorityGroup.USER.raw)

                //4. OpenAPI，放行
                .pathMatchers(
                        "/swagger-ui.html",
                        "/v3/api-docs-gateway",
                        "/swagger-config.json",
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/webjars/**").permitAll()

                //5. 服务状态，放行
                .pathMatchers("/actuator/**").permitAll()

                //6. 其它，拦截
                .anyExchange().permitAll()

                //配置过滤器
                .and()
                //1. 微信登录
                .addFilterAt(weixinFilter(reactiveAuthenticationManager, jwtUtils), SecurityWebFiltersOrder.AUTHENTICATION)
                //2. 管理员用户名密码验证
                .addFilterAt(usernamePasswordFilter(reactiveAuthenticationManager, jwtUtils), SecurityWebFiltersOrder.AUTHENTICATION)
                //3. token验证
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                //4. 签名验证
                //.addFilterBefore(signatureFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .csrf().disable().exceptionHandling()

                //跨域配置
                .and()
                .cors()
                .configurationSource(urlBasedCorsConfigurationSource());
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
