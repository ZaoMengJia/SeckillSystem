package com.zaomengjia.auth.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 15:16
 */
public final class ResponseWriter {
    public static Mono<Void> writeResponse(ServerWebExchange exchange, Object o) {
        String s = JSON.toJSONString(o, SerializerFeature.WriteMapNullValue);
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().getHeaders().set("Content-Type", "text/json;charset=utf-8");
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
