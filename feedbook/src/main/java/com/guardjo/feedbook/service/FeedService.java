package com.guardjo.feedbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.InvalidRequestException;
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

	/**
	 * <p>id에 해당하는 Feed의 title과 content를 갱신한다.</p>
	 * <i>단, account가 본인이 일치한 경우에만 갱신함</i>
	 * @param id Feed 식별키
	 * @param title 갱신할 Feed의 제목
	 * @param content 갱신할 Feed의 내용
	 * @param account 갱신 요청 계정
	 */
	public void updateFeed(long id, String title, String content, Account account) {
		Feed feed = feedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Feed.class, id));

		if (feed.getAccount().equals(account)) {
			feed.setTitle(title);
			feed.setContent(content);
		} else {
			log.warn("Invalid Update Feed, feedId = {}, account = {}", feed.getId(), account.getUsername());
			throw new InvalidRequestException();
		}

		log.info("Updated Feed, feedId = {}", feed.getId());
	}

	/**
	 * <p>특정 Feed를 삭제한다.</p>
	 * <i>단, account가 본인이 일치한 경우에만 삭제함</i>
	 * @param id Feed 식별키
	 * @param account 삭제 요청자
	 */
	public void deleteFeed(long id, Account account) {
		Feed feed = feedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Feed.class, id));

		if (feed.getAccount().equals(account)) {
			feedRepository.delete(feed);
		} else {
			log.warn("Invalid Delete Feed, feedId = {}, account = {}", feed.getId(), account.getUsername());
			throw new InvalidRequestException();
		}

		log.info("Deleted Feed, feedId = {}", id);
	}

	private Feed createNewFeed(String title, String content, Account account) {
		return Feed.builder()
			.title(title)
			.content(content)
			.account(account)
			.build();
	}
}
