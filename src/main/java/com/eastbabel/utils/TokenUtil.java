package com.eastbabel.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.eastbabel.bo.login.TokenBo;
import com.eastbabel.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * token工具类（生成、验证）
 */
@Slf4j
public class TokenUtil {

    private static final long TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;//token到期时间一周，毫秒为单位
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60;//RefreshToken到期时间为一个月，秒为单位
    private static final String ISSUER = "eastbabel";
    private static final String SIGN_TIME = "sign_time";
    private static final String USER_ID = "user_id";
    private static final String USER_REV = "user_rev";

    public static String signAuthToken(Integer userId, String salt) {
        return getToken(userId, salt, TOKEN_EXPIRE_TIME);
    }

    public static String signRefreshToken(Integer userId, String salt) {
        return getToken(userId, salt, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public static TokenBo signTokens(Integer userId, String tokenSalt, String refreshTokenSalt) {
        String authToken = signAuthToken(userId, tokenSalt);
        String refreshToken = signRefreshToken(userId, refreshTokenSalt);
        return new TokenBo(authToken, refreshToken);
    }

    private static String getToken(Integer userId, String salt, long expireTime) {
        String token = null;
        Date expireAt = new Date(System.currentTimeMillis() + expireTime);
        token = JWT.create()
                .withIssuer(ISSUER)//发行人
                .withClaim(USER_ID, userId)//存放数据
                .withClaim(SIGN_TIME, System.currentTimeMillis())
                .withExpiresAt(expireAt)//过期时间
                .sign(Algorithm.HMAC256(salt));
        return token;
    }

    public static void verify(String token, String trustToken) {
        if (StringUtils.isBlank(token)) {
            throw new CustomException("未登录");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(trustToken)).withIssuer(ISSUER).build();//创建token验证器
        DecodedJWT decodedJWT;
        decodedJWT = jwtVerifier.verify(token);
        new CustomException("登陆过期").throwIf(new Date().after(decodedJWT.getExpiresAt()));
    }

    public static Integer getUserId(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim(USER_ID).asInt();

        } catch (JWTCreationException e) {
            return null;
        }
    }

    public static Integer getUserRev(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim(USER_REV).asInt();

        } catch (JWTCreationException e) {
            return null;
        }
    }
}
