package com.guardjo.feedbook.util;

import com.guardjo.feedbook.config.JwtProperties;
import com.guardjo.feedbook.exception.CustomAuthenticationException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.AccountAccessInfo;
import com.guardjo.feedbook.repository.cache.AccountAccessInfoRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {
    private final Key jwtSecret;
    private final long expiredTime;
    private final String claimKeyName;
    private final String headerPrefix;
    private final AccountAccessInfoRepository accessInfoRepository;

    public JwtProvider(JwtProperties jwtProperties, AccountAccessInfoRepository accessInfoRepository) {
        this.jwtSecret = encodeKey(jwtProperties.secret());
        this.expiredTime = jwtProperties.expiredTime();
        this.claimKeyName = jwtProperties.claimKeyName();
        this.headerPrefix = jwtProperties.headerPrefix();
        this.accessInfoRepository = accessInfoRepository;
    }

    /**
     * 사용자 식별키를 기준으로 JWT 토큰을 발급한다.
     *
     * @param account 사용자 계정 Entity
     * @return JWT 토큰
     */
    public String createToken(Account account) {
        String username = account.getUsername();
        Claims claims = Jwts.claims();
        claims.put(claimKeyName, username);
        Date now = new Date();

        String token = Jwts.builder()
                .claim(claimKeyName, username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();

        AccountAccessInfo accessInfo = AccountAccessInfo.builder()
                .id(username)
                .token(token)
                .build();

        accessInfoRepository.save(accessInfo);

        return accessInfo.getToken();
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @param token 만료 여부 확인할 JWT 토큰
     * @return 만료 여부
     */
    public boolean isExpired(String token) {
        boolean expired = false;
        try {
            Claims claims = getClaims(token);
            expired = claims.getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            expired = true;
        }

        if (expired) {
            // 토큰이 만료 되었을 경우 계정 접속 정보 삭제
            Optional<AccountAccessInfo> accountAccessInfo = accessInfoRepository.findByToken(token);
            accountAccessInfo.ifPresent(accessInfoRepository::delete);
        }

        return expired;
    }

    /**
     * 토큰 내 username 조회
     *
     * @param token JWT 토큰
     * @return 토큰 내 사용자 식별값 반환
     */
    public String getUsername(String token) {
        Claims claims = getClaims(token);

        String username = (String) claims.get(claimKeyName);

        AccountAccessInfo accessInfo = accessInfoRepository.findById(username)
                .orElseThrow(() -> new CustomAuthenticationException("세션 정보가 존재하지 않습니다."));

        if (!accessInfo.getToken().equals(convertTokenWithoutPrefix(token))) {
            throw new CustomAuthenticationException("세션 정보가 올바르지 않습니다.");
        }

        return username;
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

    /*
    JWT 토큰 헤더 prefix값을 제거한 토큰 본문 반환
     */
    private String convertTokenWithoutPrefix(String token) {
        return token.replace(headerPrefix, "");
    }
}
