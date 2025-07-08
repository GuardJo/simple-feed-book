package com.guardjo.feedbook.repository.cache;

import com.guardjo.feedbook.model.domain.AccountAccessInfo;
import com.guardjo.feedbook.util.TestDataGenerator;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountAccessInfoRepositoryTest {
    @Autowired
    private AccountAccessInfoRepository accountAccessInfoRepository;

    private final AccountAccessInfo TEST_ACCESS_INFO = TestDataGenerator.accountAccessInfo("Tester", "test-token");

    @Container
    @ServiceConnection
    static RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.2.5"));

    @BeforeEach
    void setUp() {
        accountAccessInfoRepository.save(TEST_ACCESS_INFO);
    }

    @AfterEach
    void tearDown() {
        accountAccessInfoRepository.deleteAll();
    }

    @DisplayName("계정명에 해당하는 AccountAccessInfo Entity 조회")
    @Test
    void test_findById() {
        Optional<AccountAccessInfo> actual = accountAccessInfoRepository.findById(TEST_ACCESS_INFO.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(TEST_ACCESS_INFO);
    }

    @DisplayName("특정 토큰 데이터를 지닌 AccountAccessInfo Entity 삭제")
    @Test
    void test_deleteByToken() {
        String token = TEST_ACCESS_INFO.getToken();

        Optional<AccountAccessInfo> actual = accountAccessInfoRepository.findByToken(token);
        AccountAccessInfo accessInfo = actual.orElseThrow();

        accountAccessInfoRepository.delete(accessInfo);
    }
}