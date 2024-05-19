package com.guardjo.feedbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedComment;
import com.guardjo.feedbook.repository.FeedCommentRepository;
import com.guardjo.feedbook.repository.FeedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedCommentService {
	private final FeedCommentRepository feedCommentRepository;
	private final FeedRepository feedRepository;

	/**
	 * 신규 댓글을 저장한다
	 * @param content 댓글 내용
	 * @param account 댓글 작성 계정
	 * @param feedId 댓글을 저장할 Feed 식별자
	 */
	public void createNewComment(String content, Account account, long feedId) {
		Feed feed = feedRepository.getReferenceById(feedId);

		FeedComment comment = FeedComment.builder()
			.content(content)
			.feed(feed)
			.account(account)
			.build();

		feedCommentRepository.save(comment);

		log.info("Save New Comment, feedId = {}, account = {}", feedId, account.getUsername());
	}

	/**
	 * Feed 별 댓글 목록을 Pagination 으로 반환한다.
	 * @param pageable 조회할 Pagination 속성
	 * @param feedId 조회할 댓글의 원본 Feed 식별키
	 * @return 조화된 FeedComment Page
	 */
	public Page<FeedComment> findAllFeedComments(Pageable pageable, long feedId) {
		log.info("Find all feedComment, feedId = {}, page = {}, pageSize = {}", feedId, pageable.getPageNumber(), pageable.getPageSize());

		return feedCommentRepository.findAllByFeedId(feedId, pageable);
	}
}
