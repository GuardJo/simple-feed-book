package com.guardjo.feedbook.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.FeedCommentCreateRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.service.FeedCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequiredArgsConstructor
public class FeedCommentController {
	private final FeedCommentService feedCommentService;

	@PostMapping(UrlContext.FEED_COMMENTS_URL)
	public BaseResponse<String> saveFeedComment(@RequestBody FeedCommentCreateRequest request,
		@AuthenticationPrincipal AccountPrincipal principal) {
		Account account = principal.getAccount();

		log.info("POST : {}, account = {}, feedId = {}", UrlContext.FEED_COMMENTS_URL, account, request.feedId());

		if (request.isValid()) {
			feedCommentService.createNewComment(request.content(), account, request.feedId());
		} else {
			throw new IllegalArgumentException("Content is Empty");
		}

		return BaseResponse.defaultSuccesses();
	}
}
