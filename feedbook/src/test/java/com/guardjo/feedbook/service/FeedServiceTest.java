package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.InvalidRequestException;
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

	@DisplayName("기존 피드 수정 테스트")
	@Test
	void test_updateFeed() {
		long feedId = 1L;
		String title = "test";
		String content = "modified";
		Account account = TestDataGenerator.account(1L, "test123");
		Feed oldFeed = TestDataGenerator.feed(feedId, "old title", "old content", account);

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.of(oldFeed));

		assertThatCode(() -> feedService.updateFeed(feedId, title, content, account)).doesNotThrowAnyException();

		then(feedRepository).should().findById(eq(feedId));
	}

	@DisplayName("기존 피드 수정 테스트 : 기존 피드를 찾지 못했을 경우")
	@Test
	void test_updateFeed_NotFound() {
		long feedId = 1L;
		String title = "test";
		String content = "modified";
		Account account = TestDataGenerator.account(1L, "test123");

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.empty());

		assertThatCode(() -> feedService.updateFeed(feedId, title, content, account))
			.isInstanceOf(EntityNotFoundException.class);

		then(feedRepository).should().findById(eq(feedId));
	}

	@DisplayName("기존 피드 수정 테스트 : 수정 권한이 없을 경우")
	@Test
	void test_updateFeed_Invalid() {
		long feedId = 1L;
		String title = "test";
		String content = "modified";
		Account requester = TestDataGenerator.account(1L, "test123");
		Account account = TestDataGenerator.account(2L, "test111");
		Feed oldFeed = TestDataGenerator.feed(feedId, "old title", "old content", account);

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.of(oldFeed));

		assertThatCode(() -> feedService.updateFeed(feedId, title, content, requester))
			.isInstanceOf(InvalidRequestException.class);

		then(feedRepository).should().findById(eq(feedId));
	}

	@DisplayName("기존 피드 삭제 테스트 : 정상")
	@Test
	void test_deleteFeed() {
		long feedId = 1L;
		Account requester = TestDataGenerator.account(1L, "test123");
		Feed deleteFeed = TestDataGenerator.feed(feedId, "title", "content", requester);

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.of(deleteFeed));
		willDoNothing().given(feedRepository).delete(eq(deleteFeed));

		assertThatCode(() -> feedService.deleteFeed(feedId, requester)).doesNotThrowAnyException();

		then(feedRepository).should().findById(eq(feedId));
		then(feedRepository).should().delete(eq(deleteFeed));
	}

	@DisplayName("기존 피드 삭제 테스트 : 해당 피드를 찾지 못했을 경우")
	@Test
	void test_deleteFeed_NotFound() {
		long feedId = 1L;
		Account requester = TestDataGenerator.account(1L, "test123");

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.empty());

		assertThatCode(() -> feedService.deleteFeed(feedId, requester)).isInstanceOf(EntityNotFoundException.class);

		then(feedRepository).should().findById(eq(feedId));
	}

	@DisplayName("기존 피드 삭제 테스트 : 권한없는 요청자인 경우")
	@Test
	void test_deleteFeed_InvalidUser() {
		long feedId = 1L;
		Account requester = TestDataGenerator.account(1L, "test123");
		Account owner = TestDataGenerator.account(2L, "owner");
		Feed deleteFeed = TestDataGenerator.feed(feedId, "title", "content", owner);

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.of(deleteFeed));

		assertThatCode(() -> feedService.deleteFeed(feedId, requester)).isInstanceOf(InvalidRequestException.class);

		then(feedRepository).should().findById(eq(feedId));
	}
}