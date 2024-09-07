package com.hmdp.util;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author FENGXIN
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtHelper {
    
    // 有效时间, 单位毫秒
    private long tokenExpiration;
    
    // 使用HS512算法生成密钥
    private static final SecretKey tokenSignKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    // 生成token字符串
    public String createToken(Long userId) {
        return Jwts.builder()
                .subject("YYGH-USER")
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000 * 60))
                .claim("userId", userId)
                .signWith(tokenSignKey)
                .compact();
    }
    
    // 从token字符串获取userid
    public Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(tokenSignKey)
                .build ()
                .parseSignedClaims (token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
    }
    
    // 判断token是否有效
    public boolean isExpiration(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .build ()
                    .parseSignedClaims (token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            // 解析失败或过期异常，返回 true 表示无效
            return true;
        }
    }
}
