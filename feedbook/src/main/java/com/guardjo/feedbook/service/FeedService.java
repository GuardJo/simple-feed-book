package com.guardjo.feedbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.repository.FeedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedService {
	private final FeedRepository feedRepository;

	/**
	 * 신규 피드를 저장한다.
	 * @param title 신규 피드 제목
	 * @param content 신규 피드 내용
	 * @param account 신규 피드 생성자
	 */
	public void saveFeed(String title, String content, Account account) {
		Feed newFeed = createNewFeed(title, content, account);

		feedRepository.save(newFeed);

		log.info("Save New Feed, title = {}, id = {}", newFeed.getTitle(), newFeed.getId());
	}

	private Feed createNewFeed(String title, String content, Account account) {
		return Feed.builder()
			.title(title)
			.content(content)
			.account(account)
			.build();
	}
}
