package com.guardjo.feedbook.controller.response;

import com.guardjo.feedbook.model.domain.FeedComment;
import com.guardjo.feedbook.util.DateUtils;

public record FeedCommentDto(
	long id,
	String author,
	String createTime,
	String content
) {
	public static FeedCommentDto from(FeedComment entity) {
		return new FeedCommentDto(
			entity.getId(),
			entity.getAccount().getNickname(),
			DateUtils.toString(entity.getCreatedAt()),
			entity.getContent()
		);
	}
}
