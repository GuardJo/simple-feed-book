package com.guardjo.feedbook.controller;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.FeedCreateRequest;
import com.guardjo.feedbook.controller.request.FeedModifyRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedDto;
import com.guardjo.feedbook.controller.response.FeedPageDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping(UrlContext.FEEDS_URL)
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
    public BaseResponse<FeedPageDto> getFeedPage(@PageableDefault Pageable pageable, @AuthenticationPrincipal AccountPrincipal principal) {
        log.info("GET : {}, username = {}", UrlContext.FEEDS_URL, principal.getUsername());

        Account account = principal.getAccount();

        Page<Feed> feeds = feedService.getAllFeeds(pageable);
        List<FeedDto> feedDtos = feeds.stream()
                .map(feed -> FeedDto.from(feed, account))
                .toList();

        FeedPageDto feedPageDto = new FeedPageDto(feedDtos, feeds.getTotalPages());

        return BaseResponse.<FeedPageDto>builder()
                .status(HttpStatus.OK.name())
                .body(feedPageDto)
                .build();
    }

    @PatchMapping(UrlContext.FEEDS_URL)
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
    public BaseResponse<String> deleteFeed(@PathVariable long feedId, @AuthenticationPrincipal AccountPrincipal principal) {
        log.info("DELETE : {}/{}, username = {}", UrlContext.FEEDS_URL, feedId, principal.getUsername());

        feedService.deleteFeed(feedId, principal.getAccount());

        return BaseResponse.defaultSuccesses();
    }
}
