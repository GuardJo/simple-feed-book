package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

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

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedComment;
import com.guardjo.feedbook.repository.FeedCommentRepository;
import com.guardjo.feedbook.repository.FeedRepository;
import com.guardjo.feedbook.util.TestDataGenerator;

@ExtendWith(MockitoExtension.class)
class FeedCommentServiceTest {
	@Mock
	private FeedRepository feedRepository;
	@Mock
	private FeedCommentRepository feedCommentRepository;

	@InjectMocks
	private FeedCommentService feedCommentService;

	private final static Account TEST_ACCOUNT = TestDataGenerator.account("tester");
	private final static Feed TEST_FEED = TestDataGenerator.feed(1, "Test Feed", "Test", TEST_ACCOUNT);

	@DisplayName("신규 댓글 저장 테스트")
	@Test
	void test_createNewComment() {
		String content = "New Comment";
		long feedId = TEST_FEED.getId();
		FeedComment actual = FeedComment.builder()
			.feed(TEST_FEED)
			.account(TEST_ACCOUNT)
			.content(content)
			.build();

		ArgumentCaptor<FeedComment> commentCaptor = ArgumentCaptor.forClass(FeedComment.class);

		given(feedRepository.getReferenceById(eq(feedId))).willReturn(TEST_FEED);
		given(feedCommentRepository.save(commentCaptor.capture())).willReturn(any(FeedComment.class));

		assertThatCode(() -> feedCommentService.createNewComment(content, TEST_ACCOUNT, feedId)).doesNotThrowAnyException();

		assertThat(actual.toString()).isEqualTo(commentCaptor.getValue().toString());

		then(feedRepository).should().getReferenceById(eq(feedId));
		then(feedCommentRepository).should().save(any(FeedComment.class));
	}

	@DisplayName("Feed 별 FeedComment Page 조회 테스트")
	@Test
	void test_findAllFeedComments() {
		Pageable pageable = PageRequest.of(0, 10);
		List<FeedComment> feedCommentList = List.of(TestDataGenerator.feedComment("Test", TEST_FEED, TEST_ACCOUNT));
		Page<FeedComment> expected = new PageImpl<>(feedCommentList);

		given(feedCommentRepository.findAllByFeedId(eq(TEST_FEED.getId()), eq(pageable))).willReturn(expected);

		Page<FeedComment> actual = feedCommentService.findAllFeedComments(pageable, TEST_FEED.getId());

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);
		assertThat(actual.getContent()).isEqualTo(feedCommentList);

		then(feedCommentRepository).should().findAllByFeedId(eq(TEST_FEED.getId()), eq(pageable));
	}
}