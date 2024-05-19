package com.guardjo.feedbook.controller.request;

import org.springframework.util.StringUtils;

public record FeedCommentCreateRequest(
	long feedId,
	String content
) {
	public boolean isValid() {
		return StringUtils.hasText(this.content);
	}
}
