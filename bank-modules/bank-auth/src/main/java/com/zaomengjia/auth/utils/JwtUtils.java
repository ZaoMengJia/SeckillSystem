package com.zaomengjia.auth.utils;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zaomengjia.auth.constant.AuthorityGroup;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 14:55
 */
@Component
public final class JwtUtils {

    @Value("${auth.secret}")
    private String secret;

    public String getJwt(List<AuthorityGroup> authorityGroup, long userId) {
        return JWT.create()
                .withClaim("role", JSONObject.toJSONString(authorityGroup))
                .withClaim("timestamp", System.currentTimeMillis())
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC384(secret));
    }

    public Map<String, Claim> decodeJwt(@NonNull String jwt) {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC384(secret)).build().verify(jwt);
            return verify.getClaims();
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return null;
        }
    }
}
