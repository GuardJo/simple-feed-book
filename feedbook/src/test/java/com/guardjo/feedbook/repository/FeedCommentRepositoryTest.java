package com.guardjo.feedbook.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.guardjo.feedbook.config.JpaConfig;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedComment;
import com.guardjo.feedbook.util.TestDataGenerator;

@DataJpaTest
@Import(JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedCommentRepositoryTest {
	@Autowired
	private FeedCommentRepository feedCommentRepository;
	@Autowired
	private FeedRepository feedRepository;
	@Autowired
	private AccountRepository accountRepository;

	private final List<FeedComment> testData = new ArrayList<>();

	private final static int TEST_DATA_SIZE = 5;

	@BeforeEach
	void setUp() {
		Account account = TestDataGenerator.account("Tester");
		Feed feed = TestDataGenerator.feed("Test Feed", account);

		accountRepository.save(account);
		feedRepository.save(feed);

		for (int i = 0; i < TEST_DATA_SIZE; i++) {
			FeedComment feedComment = TestDataGenerator.feedComment("test comment " + i, feed, account);
			testData.add(feedCommentRepository.save(feedComment));
		}
	}

	@AfterEach
	void tearDown() {
		feedCommentRepository.deleteAll();
		feedRepository.deleteAll();
		accountRepository.deleteAll();

		testData.clear();
	}

	@DisplayName("식별키를 통한 단일 FeedComment 조회")
	@Test
	void test_findById() {
		FeedComment expected = testData.get(0);
		long id = expected.getId();

		FeedComment actual = feedCommentRepository.findById(id).orElseThrow();

		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("Feed 별 FeedComment 조회")
	@Test
	void test_findAllByFeedId() {
		Feed feed = testData.get(0).getFeed();
		List<FeedComment> expected = testData.stream()
			.filter(feedComment -> (feedComment.getFeed().equals(feed)))
			.toList();

		Pageable pageable = PageRequest.of(0, 10);

		Page<FeedComment> feedCommentPage = feedCommentRepository.findAllByFeedId(feed.getId(), pageable);
		List<FeedComment> actual = feedCommentPage.getContent();

		assertThat(actual.size()).isEqualTo(expected.size());
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("신규 FeedComment 저장")
	@Test
	void test_saveNewFeedComment() {
		Feed feed = testData.get(0).getFeed();
		Account account = testData.get(0).getAccount();
		String content = "New Comment";
		FeedComment newComment = FeedComment.builder()
			.feed(feed)
			.account(account)
			.content(content)
			.build();

		FeedComment actual = feedCommentRepository.save(newComment);

		assertThat(actual.getId()).isNotNull();
		assertThat(actual).isEqualTo(newComment);
	}
}