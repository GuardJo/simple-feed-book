package com.guardjo.feedbook.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.FeedCreateRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.service.FeedService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
