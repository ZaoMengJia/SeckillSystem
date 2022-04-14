package com.zaomengjia.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.gateway.constant.AuthorityGroup;
import com.zaomengjia.gateway.exception.TokenErrorException;
import com.zaomengjia.gateway.pojo.JwtToken;
import com.zaomengjia.gateway.utils.JwtUtils;
import com.zaomengjia.common.utils.RedisUtils;
import io.netty.util.internal.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 22:04
 */
public class JwtFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    private final RedisUtils redisUtils;

    private final long expire;

    public JwtFilter(JwtUtils jwtUtils, RedisUtils redisUtils, long expire) {
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
        this.expire = expire;
    }

    private Mono<Void> chainFilterAndSetHeaders(WebFilterChain chain, ServerWebExchange exchange, LinkedHashMap<String, String> headerMap) {
        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpHeader.set(entry.getKey(), entry.getValue());
            }
        };

        ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        ServerWebExchange build = exchange.mutate().request(newRequest).build();
        return chain.filter(build);
    }

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if(antPathMatcher.matchStart("/auth/**", exchange.getRequest().getURI().getPath())) {
            return chain.filter(exchange);
        }

        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(!StringUtil.isNullOrEmpty(jwt)) {
            if(!jwt.contains("Bearer ")) {
                return chain.filter(exchange);
            }

            jwt = jwt.substring(7);

            //1. 判断jwt是不是正确的
            Map<String, Claim> claimMap = jwtUtils.decodeJwt(jwt);
            if(claimMap == null) {
                throw new TokenErrorException();
            }

            //2. 判断过期了没有
            Long timestamp = claimMap.get("timestamp").asLong();
            long now = System.currentTimeMillis();
            if(timestamp == null || now - timestamp > expire * 1000) {
                throw new TokenErrorException();
            }

            //3.判断是不是恶意访问
            //3.1 在不在黑名单里
            String userId = claimMap.get("userId").asString();
            String requestKey = "user::request-time::" + userId;
            Integer requestTime = (Integer) redisUtils.get(requestKey);
            if(requestTime != null && requestTime > 400) {
                throw new AppException(ResultCode.HAVE_A_REST_PLEASE);
            }
            //3.2 设置这次的访问
            redisUtils.incr(requestKey);
            redisUtils.expire(requestKey, 30 * 60);

            //4. 授权
            List<AuthorityGroup> authorityGroup = JSON.parseArray(claimMap.get("role").asString(), AuthorityGroup.class);
            JwtToken authentication = new JwtToken(jwt, authorityGroup.stream().map(AuthorityGroup::getAuthority).collect(Collectors.toList()));
            authentication.setAuthenticated(true);

            LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
            headerMap.put(RequestHeaderKey.AUTHORIZATION, jwt);
            return chainFilterAndSetHeaders(chain, exchange, headerMap).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        return chain.filter(exchange);
    }
}
