package com.guardjo.feedbook.util;

import com.guardjo.feedbook.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key jwtSecret;
    private final long expiredTime;
    private final String claimKeyName;
    private final String headerPrefix;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtSecret = encodeKey(jwtProperties.secret());
        this.expiredTime = jwtProperties.expiredTime();
        this.claimKeyName = jwtProperties.claimKeyName();
        this.headerPrefix = jwtProperties.headerPrefix();
    }

    /**
     * 사용자 식별키를 기준으로 JWT 토큰을 발급한다.
     *
     * @param username 사용자 식별아이디
     * @return JWT 토큰
     */
    public String createToken(String username) {
        Claims claims = Jwts.claims();
        claims.put(claimKeyName, username);
        Date now = new Date();

        return Jwts.builder()
                .claim(claimKeyName, username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @param token 만료 여부 확인할 JWT 토큰
     * @return 만료 여부
     */
    public boolean isExpired(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 토큰 내 username 조회
     *
     * @param token JWT 토큰
     * @return 토큰 내 사용자 식별값 반환
     */
    public String getUsername(String token) {
        Claims claims = getClaims(token);

        return (String) claims.get(claimKeyName);
    }

    private Claims getClaims(String token) {
        token = token.replace(headerPrefix, "");

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.jwtSecret)
                .build();

        return jwtParser.parseClaimsJws(token)
                .getBody();
    }

    private Key encodeKey(String plainKey) {
        return Keys.hmacShaKeyFor(plainKey.getBytes(StandardCharsets.UTF_8));
    }
}
