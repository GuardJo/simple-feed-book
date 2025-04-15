package com.guardjo.feedbook.controller;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.docs.FeedApiDoc;
import com.guardjo.feedbook.controller.request.FeedCreateRequest;
import com.guardjo.feedbook.controller.request.FeedModifyRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedDto;
import com.guardjo.feedbook.controller.response.FeedPageDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.service.FeedAlarmService;
import com.guardjo.feedbook.service.FeedService;
import com.guardjo.feedbook.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequiredArgsConstructor
public class FeedController implements FeedApiDoc {
    private final FeedService feedService;
    private final FeedAlarmService feedAlarmService;

    @PostMapping(UrlContext.FEEDS_URL)
    @Override
    public BaseResponse<String> createFeed(@RequestBody FeedCreateRequest feedCreateRequest, @AuthenticationPrincipal AccountPrincipal principal) {
        log.info("POST : {}, username = {}", UrlContext.FEEDS_URL, principal.getUsername());

        if (!feedCreateRequest.isValid()) {
            throw new IllegalArgumentException("Request Data is Null");
        }

        Account account = principal.getAccount();

        feedService.saveFeed(feedCreateRequest.title(), feedCreateRequest.content(), account);

        return BaseResponse.defaultSuccesses();
    }

    @GetMapping(UrlContext.FEEDS_URL)
    @Override
    public BaseResponse<FeedPageDto> getFeedPage(@AuthenticationPrincipal AccountPrincipal principal,
                                                 @RequestParam("page") int pageNumber,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize) {
        log.info("GET : {}, username = {}", UrlContext.FEEDS_URL, principal.getUsername());

        Account account = principal.getAccount();

        Pageable pageable = PaginationUtils.fromSortByCreatedAtDesc(pageNumber, pageSize);
        Page<FeedDto> feeds = feedService.getAllFeeds(pageable, account);

        FeedPageDto feedPageDto = initFeedPageDto(feeds);

        return BaseResponse.<FeedPageDto>builder()
                .status(HttpStatus.OK.name())
                .body(feedPageDto)
                .build();
    }

    @GetMapping(UrlContext.MY_FEEDS_URL)
    @Override
    public BaseResponse<FeedPageDto> getMyFeedPage(@AuthenticationPrincipal AccountPrincipal principal,
                                                   @RequestParam("page") int pageNumber,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize) {
        log.info("GET : {}, username = {}", UrlContext.MY_FEEDS_URL, principal.getUsername());

        Account account = principal.getAccount();

        Pageable pageable = PaginationUtils.fromSortByCreatedAtDesc(pageNumber, pageSize);
        Page<FeedDto> feeds = feedService.getMyFeeds(pageable, account);

        FeedPageDto feedPageDto = initFeedPageDto(feeds);

        return BaseResponse.<FeedPageDto>builder()
                .status(HttpStatus.OK.name())
                .body(feedPageDto)
                .build();
    }

    @PatchMapping(UrlContext.FEEDS_URL)
    @Override
    public BaseResponse<String> updateFeed(@RequestBody FeedModifyRequest feedModifyRequest, @AuthenticationPrincipal AccountPrincipal principal) {
        log.info("PATCH : {}, username = {}", UrlContext.FEEDS_URL, principal.getUsername());

        if (!feedModifyRequest.isValid()) {
            throw new IllegalArgumentException("Feed Content or Id is Null");
        }

        Account account = principal.getAccount();

        feedService.updateFeed(feedModifyRequest.feedId(), feedModifyRequest.title(), feedModifyRequest.content(), account);

        return BaseResponse.defaultSuccesses();
    }

    @DeleteMapping(UrlContext.FEEDS_URL + "/{feedId}")
    @Override
    public BaseResponse<String> deleteFeed(@PathVariable long feedId, @AuthenticationPrincipal AccountPrincipal principal) {
        log.info("DELETE : {}/{}, username = {}", UrlContext.FEEDS_URL, feedId, principal.getUsername());

        feedService.deleteFeed(feedId, principal.getAccount());

        return BaseResponse.defaultSuccesses();
    }

    @PutMapping(UrlContext.FAVORITE_FEEDS_URL + "/{feedId}")
    @Override
    public BaseResponse<String> updateFavoriteFeed(@PathVariable long feedId, @AuthenticationPrincipal AccountPrincipal principal) {
        Account account = principal.getAccount();
        log.info("PUT : {}/{}, username = {}", UrlContext.FAVORITE_FEEDS_URL, feedId, principal.getUsername());

        feedService.updateFeedFavorite(feedId, account);
        feedAlarmService.saveNewAlarm(AlarmType.FAVORITE, new AlarmArgs(account.getId()), feedId);

        return BaseResponse.defaultSuccesses();
    }

    private FeedPageDto initFeedPageDto(Page<FeedDto> feeds) {
        List<FeedDto> feedDtos = feeds.getContent();

        return new FeedPageDto(feedDtos, feeds.getTotalPages());
    }
}
