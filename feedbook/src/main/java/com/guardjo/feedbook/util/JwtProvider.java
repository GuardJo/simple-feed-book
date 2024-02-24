package com.guardjo.feedbook.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.guardjo.feedbook.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private final Key jwtSecret;
	private final long expiredTime;
	private final String claimKeyName;

	public JwtProvider(JwtProperties jwtProperties) {
		this.jwtSecret = encodeKey(jwtProperties.secret());
		this.expiredTime = jwtProperties.expiredTime();
		this.claimKeyName = jwtProperties.claimKeyName();
	}

	/**
	 * 사용자 식별키를 기준으로 JWT 토큰을 발급한다.
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

	private Key encodeKey(String plainKey) {
		return Keys.hmacShaKeyFor(plainKey.getBytes(StandardCharsets.UTF_8));
	}
}
