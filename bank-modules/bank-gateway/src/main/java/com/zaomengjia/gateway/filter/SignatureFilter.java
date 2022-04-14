package com.zaomengjia.gateway.filter;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.MD5Utils;
import com.zaomengjia.common.utils.RedisUtils;
import io.netty.util.internal.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/11 17:12
 */
public class SignatureFilter implements WebFilter {

    private final String appKey;

    private final long signatureExpireTime;

    private final RedisUtils redisUtils;

    public SignatureFilter(RedisUtils redisUtils, String appKey, long signatureExpireTime) {
        this.redisUtils = redisUtils;
        this.appKey = appKey;
        this.signatureExpireTime = signatureExpireTime;
    }

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String requestSignature = headers.getFirst(RequestHeaderKey.SIGNATURE);
        String requestTimestamp = headers.getFirst(RequestHeaderKey.TIMESTAMP);
        String requestNonce = headers.getFirst(RequestHeaderKey.NONCE);

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        long count = Stream.of("/auth/**", "/admin/**", "/weixin/**")
                .filter(path -> antPathMatcher.matchStart(path, request.getURI().getPath()))
                .count();
        if(count == 0 || antPathMatcher.matchStart("/", request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        if(StringUtil.isNullOrEmpty(requestSignature) || StringUtil.isNullOrEmpty(requestTimestamp) || StringUtil.isNullOrEmpty(requestNonce)) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }

        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @NotNull
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().doOnNext(buffer -> {
                    Map<String, String> map = request.getQueryParams().toSingleValueMap();
                    String body = String.valueOf(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()));
                    MediaType contentType = headers.getContentType();

                    if(request.getMethod() != HttpMethod.GET && MediaType.APPLICATION_JSON.equals(contentType)) {
                        verifySignature(body, requestNonce, requestTimestamp, requestSignature);
                    }
                    else {
                        for (String s : body.split("&")) {
                            String[] split = s.split("=");
                            if(split.length != 2) {
                                throw new AppException(ResultCode.INVALID_SIGNATURE);
                            }
                            map.put(split[0], split[1]);
                        }

                        map.remove("t");
                        String input = JSON.toJSONString(new TreeMap<>(map));
                        verifySignature(input, requestNonce, requestTimestamp, requestSignature);
                    }

                });
            }
        };

        return chain.filter(exchange.mutate().request(decorator).build());
    }


    private void verifySignature(String input, String nonce, String timestampStr, String signature) {
        //1. 检查是否过期
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (NumberFormatException e) {
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        long interval = System.currentTimeMillis() - timestamp;
        if(interval < 0 || interval > 1000 * 60 * signatureExpireTime) {
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        //2. 检查nonce是否唯一
        if(redisUtils.containKey("auth::nonce::" + nonce)) {
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        //3. 验签
        String correctSignature = getSignature(input + nonce + timestampStr + appKey);
        if(!correctSignature.equals(signature)) {
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        //4. 设置nonce报废
        redisUtils.set("auth::nonce::" + nonce, nonce, 60 * signatureExpireTime);
    }

    private String getSignature(String input) {
        String md5 = MD5Utils.toMD5(input);
        return Base64.encode(md5).replaceAll("[=/+]", "");
    }
}
