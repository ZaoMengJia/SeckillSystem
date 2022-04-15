package com.zaomengjia.gateway.filter;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.MD5Utils;
import com.zaomengjia.common.utils.RedisUtils;
import io.netty.util.internal.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.CoreSubscriber;
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
@Component
public class SignatureFilter implements GlobalFilter, Ordered {

    @Value("${auth.app-key}")
    private String appKey;

    @Value("${auth.sign-expired-time}")
    private long signatureExpireTime;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RedisUtils redisUtils;

    private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;

    public SignatureFilter(RedisUtils redisUtils, ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter) {
        this.redisUtils = redisUtils;
        this.modifyRequestBodyFilter = modifyRequestBodyFilter;
    }

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull GatewayFilterChain chain) {
        if(true) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String requestSignature = headers.getFirst(RequestHeaderKey.SIGNATURE);
        String requestTimestamp = headers.getFirst(RequestHeaderKey.TIMESTAMP);
        String requestNonce = headers.getFirst(RequestHeaderKey.NONCE);

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        long count = Stream.of("/auth/**", "/web/**", "/weixin/**")
                .filter(path -> antPathMatcher.matchStart(path, request.getURI().getPath()))
                .count();
        if(count == 0 || antPathMatcher.matchStart("/", request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        if(StringUtil.isNullOrEmpty(requestSignature) || StringUtil.isNullOrEmpty(requestTimestamp) || StringUtil.isNullOrEmpty(requestNonce)) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }



        return modifyRequestBodyFilter.apply(
                new ModifyRequestBodyGatewayFilterFactory.Config().setRewriteFunction(String.class, String.class, new RewriteFunction<String, String>() {
                    @Override
                    public Publisher<String> apply(ServerWebExchange exchange, String body) {
                        Map<String, String> map = request.getQueryParams().toSingleValueMap();
                        map.remove("t");
                        if(body == null) {
                            String input = JSON.toJSONString(new TreeMap<>(map));
                            verifySignature(input, requestNonce, requestTimestamp, requestSignature);
                        }
                        else {
                            MediaType contentType = headers.getContentType();

                            if(request.getMethod() != HttpMethod.GET && MediaType.APPLICATION_JSON.equals(contentType)) {
                                verifySignature(body, requestNonce, requestTimestamp, requestSignature);
                            }
                            else {
                                for (String s : body.split("&")) {
                                    String[] split = s.split("=");
                                    if(split.length != 2) {
                                        logger.error("签名：有问题");
                                        throw new AppException(ResultCode.INVALID_SIGNATURE);
                                    }
                                    map.put(split[0], split[1]);
                                }
                                String input = JSON.toJSONString(new TreeMap<>(map));
                                verifySignature(input, requestNonce, requestTimestamp, requestSignature);
                            }

                        }
                        return Mono.justOrEmpty(body);
                    }
                })
        )
                .filter(exchange, chain);
    }


    private void verifySignature(String input, String nonce, String timestampStr, String signature) {
        //1. 检查是否过期
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (NumberFormatException e) {
            logger.error("签名：已过期");
            throw new AppException(ResultCode.INVALID_SIGNATURE);

        }

        long interval = System.currentTimeMillis() - timestamp;
        if(interval < - 60 * 1000 || interval > 1000 * 60 * signatureExpireTime) {
            logger.error("签名：已过期 {} - {}", System.currentTimeMillis(), timestamp);
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        //2. 检查nonce是否唯一
        if(redisUtils.containKey("auth::nonce::" + nonce)) {
            logger.error("签名：nonce已被使用");
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        //3. 验签
        String correctSignature = getSignature(input + nonce + timestampStr + appKey);
        if(!correctSignature.equals(signature)) {
            logger.error("签名错误追溯：input {} 正确的签名 {}", input + nonce + timestampStr + appKey, correctSignature);
            throw new AppException(ResultCode.INVALID_SIGNATURE);
        }

        //4. 设置nonce报废
        redisUtils.set("auth::nonce::" + nonce, nonce, 60 * signatureExpireTime);
    }

    private String getSignature(String input) {
        String md5 = MD5Utils.toMD5(input);
        return Base64.encode(md5).replaceAll("[=/+]", "");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
