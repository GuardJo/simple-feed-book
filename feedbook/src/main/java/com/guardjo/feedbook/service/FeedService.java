package com.guardjo.feedbook.service;

import com.guardjo.feedbook.controller.response.FeedDto;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.InvalidRequestException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository feedRepository;

    /**
     * 신규 피드를 저장한다.
     *
     * @param title   신규 피드 제목
     * @param content 신규 피드 내용
     * @param account 신규 피드 생성자
     */
    public void saveFeed(String title, String content, Account account) {
        Feed newFeed = createNewFeed(title, content, account);

        feedRepository.save(newFeed);

        log.info("Save New Feed, title = {}, id = {}", newFeed.getTitle(), newFeed.getId());
    }

    /**
     * 저장되어 있는 전체 피드 목록을 페이징해서 반환한다.
     *
     * @param pageable 조회할 페이지 설정
     * @param account  조회 요청 계정
     * @return 페이징된 FeedDto 목록
     */
    public Page<FeedDto> getAllFeeds(Pageable pageable, Account account) {

        Page<FeedDto> feeds = feedRepository.findAllFeedDto(pageable, account);

        log.info("Found, Feed List, totalPage = {}, totalSize = {}", feeds.getTotalPages(), feeds.getTotalElements());

        return feeds;
    }

    /**
     * 특정 계정이 작성한 전체 피드 모곩을 페이징해서 반환한다.
     *
     * @param pageable 조회할 페이지 설정
     * @param account  작성자 계정
     * @return 페이징된 Feed 목록
     */
    public Page<FeedDto> getMyFeeds(Pageable pageable, Account account) {
        Page<Feed> feeds = feedRepository.findAllByAccount(pageable, account);
        List<FeedDto> feedDtoList = feeds.stream()
                .map(feed -> FeedDto.from(feed, account))
                .toList();

        log.info("Found, Feed List, totalPage = {}, totalSize = {}", feeds.getTotalPages(), feeds.getTotalElements());

        return new PageImpl<>(feedDtoList, pageable, feeds.getTotalElements());
    }

    /**
     * <p>id에 해당하는 Feed의 title과 content를 갱신한다.</p>
     * <i>단, account가 본인이 일치한 경우에만 갱신함</i>
     *
     * @param id      Feed 식별키
     * @param title   갱신할 Feed의 제목
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
     *
     * @param id      Feed 식별키
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

    /**
     * 해당하는 Feed의 favorite을 갱신한다.
     *
     * @param id      Feed 식별키
     * @param account 요청 계정
     */
    public void updateFeedFavorite(long id, Account account) {
        Feed feed = feedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Feed.class, id));

        if (feed.getFavoriteAccounts().contains(account)) {
            feed.subtractFavorite(account);
            log.info("Update subtract favorite account, feedId = {}", feed.getId());
        } else {
            feed.addFavorite(account);
            log.info("Update add favorite account, feedId = {}", feed.getId());
        }
    }

    private Feed createNewFeed(String title, String content, Account account) {
        return Feed.builder()
                .title(title)
                .content(content)
                .account(account)
                .build();
    }
}
