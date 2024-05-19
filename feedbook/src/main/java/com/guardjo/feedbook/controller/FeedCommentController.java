package com.guardjo.feedbook.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.FeedCommentCreateRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedCommentPageDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.FeedComment;
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
	public BaseResponse<String> saveFeedComment(@RequestBody FeedCommentCreateRequest request, @PathVariable("feedId") long feedId,
		@AuthenticationPrincipal AccountPrincipal principal) {
		Account account = principal.getAccount();

		log.info("POST : {}, account = {}, feedId = {}", UrlContext.FEED_COMMENTS_URL, account, feedId);

		if (request.isValid()) {
			feedCommentService.createNewComment(request.content(), account, feedId);
		} else {
			throw new IllegalArgumentException("Content is Empty");
		}

		return BaseResponse.defaultSuccesses();
	}

	@GetMapping(UrlContext.FEED_COMMENTS_URL)
	public BaseResponse<FeedCommentPageDto> getFeedComment(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@PathVariable("feedId") long feedId, @AuthenticationPrincipal AccountPrincipal principal) {
		Account account = principal.getAccount();

		log.info("GET : {}, account = {}, feedId = {}", UrlContext.FEED_COMMENTS_URL, account, feedId);

		Page<FeedComment> feedCommentPage = feedCommentService.findAllFeedComments(pageable, feedId);

		return BaseResponse.<FeedCommentPageDto>builder()
			.status(HttpStatus.OK.name())
			.body(FeedCommentPageDto.from(feedCommentPage))
			.build();
	}
}
