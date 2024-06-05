package com.guardjo.feedbook.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.guardjo.feedbook.config.JpaConfig;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.util.TestDataGenerator;

@DataJpaTest
@Import(JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedAlarmRepositoryTest {
	@Autowired
	private FeedAlarmRepository feedAlarmRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private FeedRepository feedRepository;

	private final static List<Account> TEST_ACCOUNTS = new ArrayList<>();
	private final static List<Feed> TEST_FEEDS = new ArrayList<>();
	private final static List<FeedAlarm> TEST_DATA = new ArrayList<>();
	private final static int TEST_SIZE = 5;

	@BeforeEach
	void setUp() {
		Account account1 = accountRepository.save(TestDataGenerator.account("tester1"));
		Account account2 = accountRepository.save(TestDataGenerator.account("tester2"));
		TEST_ACCOUNTS.addAll(List.of(account1, account2));
		Feed feed1 = feedRepository.save(TestDataGenerator.feed("test1", account1));
		Feed feed2 = feedRepository.save(TestDataGenerator.feed("test2", account2));
		TEST_FEEDS.addAll(List.of(feed1, feed2));

		for (int i = 0; i < TEST_SIZE; i++) {
			if (i == 0) {
				TEST_DATA.add(FeedAlarm.builder()
					.alarmType(AlarmType.FAVORITE)
					.args(new AlarmArgs(account2.getId()))
					.feed(feed2)
					.build()
				);
			} else {
				TEST_DATA.add(FeedAlarm.builder()
					.feed(feed1)
					.args(new AlarmArgs(account1.getId()))
					.alarmType(AlarmType.COMMENT)
					.build());
			}
		}

		feedAlarmRepository.saveAll(TEST_DATA);
	}

	@AfterEach
	void tearDown() {
		feedAlarmRepository.deleteAll();
		feedRepository.deleteAll();
		accountRepository.deleteAll();
		TEST_ACCOUNTS.clear();
		TEST_FEEDS.clear();
		TEST_DATA.clear();
	}

	@DisplayName("신규 알림 저장")
	@Test
	void test_saveNewFeedAlarm() {
		FeedAlarm newFeedAlarm = FeedAlarm.builder()
			.alarmType(AlarmType.COMMENT)
			.feed(TEST_FEEDS.get(0))
			.args(new AlarmArgs(TEST_ACCOUNTS.get(0).getId()))
			.build();

		FeedAlarm actual = feedAlarmRepository.save(newFeedAlarm);

		assertThat(actual).isNotNull();
		assertThat(actual.getId()).isNotNull();
		assertThat(actual).isEqualTo(newFeedAlarm);
		assertThat(feedAlarmRepository.count()).isEqualTo(TEST_SIZE + 1);
	}

	@DisplayName("계정 별 전체 알림 삭제")
	@ParameterizedTest
	@MethodSource("findAndDeleteAllByAccountIdParams")
	void test_deleteAllByAccountId(int accountIndex, int totalCount) {
		long accountId = TEST_ACCOUNTS.get(accountIndex).getId();
		feedAlarmRepository.deleteAllByFeed_Account_Id(accountId);
		long actual = feedAlarmRepository.count();

		assertThat(actual).isEqualTo(TEST_SIZE - totalCount);
	}

	@DisplayName("계정 별 알림 목록 조회")
	@ParameterizedTest
	@MethodSource("findAndDeleteAllByAccountIdParams")
	void test_findAllByAccountId(int accountIndex, int totalCount) {
		long accountId = TEST_ACCOUNTS.get(accountIndex).getId();
		Pageable pageable = PageRequest.of(0, 10);

		Page<FeedAlarm> feedAlarms = feedAlarmRepository.findAllByFeed_Account_Id(accountId, pageable);
		List<FeedAlarm> actual = feedAlarms.getContent();

		assertThat(actual).isNotNull();
		assertThat(actual.size()).isEqualTo(totalCount);
		assertThat(TEST_DATA.containsAll(actual)).isTrue();
	}

	private static Stream<Arguments> findAndDeleteAllByAccountIdParams() {
		return Stream.of(
			Arguments.of(0, 4),
			Arguments.of(1, 1)
		);
	}
}