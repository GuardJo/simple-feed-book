package com.guardjo.feedbook.model.domain;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash(value = "ACCOUNT_CACHE", timeToLive = 86400L) // 1일 동안 유지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class AccountCache {
	@Id
	private String id;
	private Account account;
}
