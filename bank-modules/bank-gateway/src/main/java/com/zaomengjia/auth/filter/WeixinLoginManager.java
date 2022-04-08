package com.zaomengjia.auth.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.auth.constant.AuthorityGroup;
import com.zaomengjia.auth.exception.LoginErrorException;
import com.zaomengjia.auth.exception.NetworkException;
import com.zaomengjia.auth.pojo.WeixinToken;
import com.zaomengjia.common.dao.WeixinUserMapper;
import com.zaomengjia.common.entity.User;
import com.zaomengjia.common.entity.WeixinUser;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
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

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.secret}")
    private String secret;

    private final WeixinUserMapper weixinUserMapper;

    public WeixinLoginManager(WeixinUserMapper weixinUserMapper) {
        this.weixinUserMapper = weixinUserMapper;
    }


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String code = authentication.getName();
        JSONObject json = obtainOpenidResponse(code);
        if(json == null) {
            throw new NetworkException();
        }

        if(json.getInteger("errcode") != null) {
            String message = json.getString("errmsg");
            logger.error("微信登录出错: {}", message);
            throw new LoginErrorException(message);
        }

        String openid = json.getString("openid");

        //保存openid
        WeixinUser user = weixinUserMapper.findByOpenid(openid);
        if(user == null) {
            user = new WeixinUser();
            user.setOpenid(openid);
            user = weixinUserMapper.save(user);
        }

        WeixinToken authenticate = new WeixinToken(code, openid, Collections.singleton(AuthorityGroup.USER.getAuthority()));
        authenticate.setAuthenticated(true);
        authenticate.setDetails(user);
        return Mono.just(authenticate);
    }

    @Nullable
    @SneakyThrows
    private JSONObject obtainOpenidResponse(String code) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(
                        Objects.requireNonNull(HttpUrl.parse("https://api.weixin.qq.com/sns/jscode2session")).newBuilder()
                                .addQueryParameter("appid", appid)
                                .addQueryParameter("secret", secret)
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
