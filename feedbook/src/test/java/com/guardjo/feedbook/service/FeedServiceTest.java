package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.repository.FeedRepository;
import com.guardjo.feedbook.util.TestDataGenerator;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {
	@Mock
	private FeedRepository feedRepository;

	@InjectMocks
	private FeedService feedService;

	@DisplayName("신규 피드 저장 테스트")
	@Test
	void test_saveFeed() {
		String title = "new feed";
		String content = "feed content";
		Account account = TestDataGenerator.account(1L, "test123");

		Feed expected = TestDataGenerator.feed(1L, title, content, account);
		ArgumentCaptor<Feed> argumentCaptor = ArgumentCaptor.forClass(Feed.class);

		given(feedRepository.save(argumentCaptor.capture())).willReturn(expected);

		assertThatCode(() -> feedService.saveFeed(title, content, account)).doesNotThrowAnyException();

		Feed saveFeed = argumentCaptor.getValue();
		assertThat(saveFeed).isNotNull();
		assertThat(saveFeed.getTitle()).isEqualTo(title);
		assertThat(saveFeed.getContent()).isEqualTo(content);
		assertThat(saveFeed.getAccount().toString()).isEqualTo(account.toString());

		then(feedRepository).should().save(any(Feed.class));
	}
}