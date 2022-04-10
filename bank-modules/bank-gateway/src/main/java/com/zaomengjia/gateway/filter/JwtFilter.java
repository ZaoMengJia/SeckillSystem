package com.zaomengjia.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.zaomengjia.gateway.constant.AuthorityGroup;
import com.zaomengjia.gateway.exception.TokenErrorException;
import com.zaomengjia.gateway.pojo.JwtToken;
import com.zaomengjia.gateway.utils.JwtUtils;
import io.netty.util.internal.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 22:04
 */
@Component
public class JwtFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    @Value("${auth.expired-time}")
    private long expire;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(!StringUtil.isNullOrEmpty(jwt)) {
            jwt = jwt.substring(7);

            //判断jwt是不是正确的
            Map<String, Claim> claimMap = jwtUtils.decodeJwt(jwt);
            if(claimMap == null) {
                throw new TokenErrorException();
            }

            //判断过期了没有
            Long timestamp = claimMap.get("timestamp").asLong();
            long now = System.currentTimeMillis();
            if(timestamp == null || now - timestamp > expire * 1000) {
                throw new TokenErrorException();
            }

            List<AuthorityGroup> authorityGroup = JSON.parseArray(claimMap.get("role").asString(), AuthorityGroup.class);
            JwtToken authentication = new JwtToken(jwt, authorityGroup.stream().map(AuthorityGroup::getAuthority).collect(Collectors.toList()));
            authentication.setAuthenticated(true);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }
}
