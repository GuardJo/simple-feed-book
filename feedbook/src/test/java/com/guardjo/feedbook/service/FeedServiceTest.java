package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.guardjo.feedbook.controller.response.FeedDto;
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

	@DisplayName("전체 피드 조회 테스트")
	@Test
	void test_getAllFeed() {
		Account account = TestDataGenerator.account(1L, "test123");

		Pageable pageable = PageRequest.of(1, 10);
		Page<FeedDto> expected = new PageImpl<>(List.of(FeedDto.from(TestDataGenerator.feed(1L, "test", "content", account), account)));

		given(feedRepository.findAllFeedDto(eq(pageable), eq(account))).willReturn(expected);

		Page<FeedDto> actual = feedService.getAllFeeds(pageable, account);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);

		then(feedRepository).should().findAllFeedDto(eq(pageable), eq(account));
	}

	@DisplayName("특정 사용자별 전체 피드 조회 테스트")
	@Test
	void test_getMyFeeds() {
		Account account = TestDataGenerator.account(1L, "test123");

		Pageable pageable = PageRequest.of(1, 10);
		Page<Feed> expected = new PageImpl<>(List.of(TestDataGenerator.feed(1L, "test", "content", account)));

		given(feedRepository.findAllByAccount(eq(pageable), eq(account))).willReturn(expected);

		Page<FeedDto> actual = feedService.getMyFeeds(pageable, account);

		assertThat(actual).isNotNull();
		assertThat(actual.getContent().size()).isEqualTo(expected.getContent().size());

		for (int i = 0; i < expected.getContent().size(); i++) {
			assertThat(actual.getContent().get(i).title()).isEqualTo(expected.getContent().get(i).getTitle());
		}

		then(feedRepository).should().findAllByAccount(eq(pageable), eq(account));
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

	@DisplayName("특정 피드의 좋아요 갱신 테스트")
	@Test
	void test_updateFeedFavorite() {
		long feedId = 1L;
		Account requester = TestDataGenerator.account(1L, "test123");

		Feed feed = TestDataGenerator.feed(feedId, "title", "content", requester);

		given(feedRepository.findById(eq(feedId))).willReturn(Optional.of(feed));

		feedService.updateFeedFavorite(feedId, requester);

		then(feedRepository).should().findById(eq(feedId));
	}
}