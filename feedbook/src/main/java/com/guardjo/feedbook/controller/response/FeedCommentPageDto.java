package com.guardjo.feedbook.controller.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guardjo.feedbook.model.domain.FeedComment;

public record FeedCommentPageDto(
	long totalPage,
	long number,
	List<FeedCommentDto> comments
) {
	public static FeedCommentPageDto from(Page<FeedComment> feedCommentPage) {
		return new FeedCommentPageDto(
			feedCommentPage.getTotalPages(),
			feedCommentPage.getNumber(),
			feedCommentPage.getContent().stream()
				.map(FeedCommentDto::from)
				.toList()
		);
	}
}
