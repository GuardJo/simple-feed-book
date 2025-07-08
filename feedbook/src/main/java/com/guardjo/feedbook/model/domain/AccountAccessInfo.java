package com.guardjo.feedbook.model.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "ACCOUNT_ACCESS_INFO", timeToLive = 86_400L)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"id", "token"}, callSuper = false)
public class AccountAccessInfo {
    @Id
    private String id;

    @Indexed
    private String token;
}
