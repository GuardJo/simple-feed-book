package com.guardjo.feedbook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
	String secret,
	long expiredTime,
	String claimKeyName
) {
}
