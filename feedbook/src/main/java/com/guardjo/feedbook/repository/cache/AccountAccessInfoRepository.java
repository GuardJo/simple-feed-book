package com.guardjo.feedbook.repository.cache;

import com.guardjo.feedbook.model.domain.AccountAccessInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountAccessInfoRepository extends CrudRepository<AccountAccessInfo, String> {
    /**
     * 주어진 토큰에 대한 access 정보를 조회한다
     *
     * @param token JWT 토큰
     */
    Optional<AccountAccessInfo> findByToken(String token);
}
