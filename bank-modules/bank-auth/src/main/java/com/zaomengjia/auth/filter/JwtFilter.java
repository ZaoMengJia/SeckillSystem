package com.zaomengjia.auth.filter;

import com.zaomengjia.auth.pojo.JwtToken;
import io.netty.util.internal.StringUtil;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 22:04
 */
@Component
public class JwtFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(!StringUtil.isNullOrEmpty(jwt)) {
            //Todo: 验证token

            JwtToken authentication = new JwtToken(jwt);
            authentication.setAuthenticated(true);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }
}
