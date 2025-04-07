package com.movietix.xiazihao.utils;

import com.movietix.xiazihao.constants.ConstantsManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET_KEY = ConstantsManager.getInstance().getSecretKey(); // 秘钥
    private static final long EXPIRATION_TIME = ConstantsManager.getInstance().getExpirationTime(); // 12小时

    /**
     * 生成JWT令牌
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 解析JWT令牌
     */
    public static Claims parseToken(String token) throws Exception {
       return Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    }
}