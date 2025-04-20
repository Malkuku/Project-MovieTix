package com.movietix.xiazihao.utils;

import com.movietix.xiazihao.constants.ConstantsManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.security.sasl.AuthenticationException;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET_KEY = ConstantsManager.getInstance().getSecretKey(); // 秘钥
    private static final String ADMIN_SECRET_KEY = ConstantsManager.getInstance().getAdminSecretKey();
    private static final long EXPIRATION_TIME = ConstantsManager.getInstance().getExpirationTime(); // 12小时

    /**
     * 生成JWT令牌
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }
    public static String createAdminToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, ADMIN_SECRET_KEY)
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
    public static Claims parseAdminToken(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(ADMIN_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证JWT令牌的userId字段
     */
    public static void verifyUserId(String token, Integer userId) throws Exception {
        Claims claims = parseToken(token);
        Integer id = (Integer) claims.get("id");
        if(!id.equals(userId)) throw new AuthenticationException("Token中的userId与请求参数不一致");
    }
}