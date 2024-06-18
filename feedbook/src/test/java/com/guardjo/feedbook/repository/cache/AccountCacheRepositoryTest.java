package com.guardjo.feedbook.repository.cache;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.ArrayList;
import java.util.List;

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

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.AccountCache;
import com.guardjo.feedbook.util.TestDataGenerator;
import com.redis.testcontainers.RedisContainer;

@DataRedisTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountCacheRepositoryTest {
	@Autowired
	private AccountCacheRepository accountCacheRepository;

	private final static int TEST_DATA_SIZE = 10;
	private final static List<AccountCache> TEST_DATA_LIST = new ArrayList<>();

	@Container
	@ServiceConnection
	static RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.2.5"));

	@BeforeEach
	void setUp() {
		for (int i = 0; i < TEST_DATA_SIZE; i++) {
			Account account = TestDataGenerator.account(i + 1, "Tester" + i);
			AccountCache accountCache = AccountCache.builder()
				.account(account)
				.build();

			TEST_DATA_LIST.add(accountCacheRepository.save(accountCache));
		}
	}

	@AfterEach
	void tearDown() {
		accountCacheRepository.deleteAll();
		TEST_DATA_LIST.clear();
	}

	@DisplayName("Account 캐시 데이터 저장")
	@Test
	void test_save() {
		Account account = TestDataGenerator.account(99L, "test222");
		AccountCache accountCache = AccountCache.builder()
			.account(account)
			.build();

		AccountCache actual = accountCacheRepository.save(accountCache);

		assertThat(actual.getId()).isNotNull();
		assertThat(accountCacheRepository.count()).isEqualTo(TEST_DATA_SIZE + 1);
	}

	@DisplayName("식별키를 통한 단일 조회")
	@Test
	void test_findById() {
		AccountCache expected = TEST_DATA_LIST.get(0);

		long id = expected.getId();

		AccountCache actual = accountCacheRepository.findById(id).orElseThrow();

		assertThat(actual).isNotNull();
		assertThat(actual.getAccount()).isNotNull();
		assertThat(actual.getAccount()).isEqualTo(expected.getAccount());
	}
}