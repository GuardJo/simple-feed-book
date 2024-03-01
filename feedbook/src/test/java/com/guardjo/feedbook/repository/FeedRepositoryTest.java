package com.guardjo.feedbook.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.guardjo.feedbook.config.JpaConfig;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.util.TestDataGenerator;

@DataJpaTest
@Import(JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedRepositoryTest {
	@Autowired
	private FeedRepository feedRepository;
	@Autowired
	private AccountRepository accountRepository;
	private final List<Feed> testFeeds = new ArrayList<>();

	private final static int TEST_DATA_SIZE = 5;

	@BeforeEach
	void setUp() {
		Account account = accountRepository.save(TestDataGenerator.account("test123"));

		for (int i = 0; i < TEST_DATA_SIZE; i++) {
			Feed feed = TestDataGenerator.feed("test_" + i, account);
			testFeeds.add(feedRepository.save(feed));
		}
	}

	@AfterEach
	void tearDown() {
		testFeeds.clear();
	}

	@DisplayName("식별키를 통한 Feed 단일 조회 테스트")
	@Test
	void test_findById() {
		Feed expected = testFeeds.get(0);

		Feed actual = feedRepository.findById(expected.getId()).orElseThrow();

		assertThat(actual).isNotNull();
		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.toString()).isEqualTo(expected.toString());
	}
}