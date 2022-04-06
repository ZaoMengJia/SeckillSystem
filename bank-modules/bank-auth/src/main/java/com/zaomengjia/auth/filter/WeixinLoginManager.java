package com.zaomengjia.auth.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.auth.pojo.WeixinToken;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 12:02
 */
@Component
public class WeixinLoginManager implements ReactiveAuthenticationManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String code = authentication.getName();

        return Mono.justOrEmpty(obtainOpenidResponse(code))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new AuthenticationServiceException("12")))
                .filter(json -> json.getInteger("errcode") != 0)
                .switchIfEmpty(Mono.error(new AuthenticationServiceException("errmsg")))
                .map(json -> {
                    String openid = json.getString("openid");
                    return new WeixinToken(code, openid, Collections.emptyList());
                });
    }

    @Nullable
    @SneakyThrows
    private JSONObject obtainOpenidResponse(String code) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(
                        Objects.requireNonNull(HttpUrl.parse("https://api.weixin.qq.com/sns/jscode2session")).newBuilder()
                                .addQueryParameter("appid", "")
                                .addQueryParameter("secret", "")
                                .addQueryParameter("js_code", code)
                                .addQueryParameter("grant_type", "authorization_code")
                                .build()
                )
                .get().build();
        Response execute = client.newCall(request).execute();
        if(execute.isSuccessful()) {
            return JSON.parseObject(Objects.requireNonNull(execute.body()).string());
        }

        logger.error("获取openid出错: {}", execute.message());
        return null;
    }

}
