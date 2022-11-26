package com.awen.shiro.config.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.awen.shiro.entity.JwtUser;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {

    //指定token过期时间（毫秒）
    private static final long EXPIRE_TIME = 20 * 60 * 1000;  //20分钟
    private static final String JWT_TOKEN_SECRET_KEY = "zngjngsoft2022";
    //↑ jwt秘钥

    /**
     * 构建一个token
     */
    public static String createJwtTokenByUser(JwtUser user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(JWT_TOKEN_SECRET_KEY);    //使用密钥进行哈希
        return JWT.create()
                .withClaim("username", user.getUsername())
                .withClaim("roles", user.getRoles())
                .withClaim("uid", user.getUid())
                .withExpiresAt(date)  //过期时间
                .sign(algorithm);
    }


    /**
     * 校验token是否正确
     */
    public static boolean verifyTokenOfUser(String token) {
        try {
            //根据密钥生成JWT校验器
            Algorithm algorithm = Algorithm.HMAC256(JWT_TOKEN_SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            //能走到这里
            return true;
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 获取用户信息
     */
    public static JwtUser getInfo(String token) {
        JwtUser user = new JwtUser();
        try {
            DecodedJWT jwt = JWT.decode(token);
            user.setUsername(jwt.getClaim("username").asString());
            user.setUid(jwt.getClaim("uid").asLong());
            user.setRoles(jwt.getClaim("roles").asList(String.class));
            return user;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 保存用户信息
     */
    public static JwtUser recreateUserFromToken(String token) {
        JwtUser user = new JwtUser();
        DecodedJWT jwt = JWT.decode(token);
        user.setUsername(jwt.getClaim("username").asString());
        user.setUid(jwt.getClaim("uid").asLong());
        user.setRoles(jwt.getClaim("roles").asList(String.class));
        return user;
    }

    /**
     * 用于判断token是否过期
     */
    public static boolean isExpire(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().getTime() < System.currentTimeMillis();
    }
}