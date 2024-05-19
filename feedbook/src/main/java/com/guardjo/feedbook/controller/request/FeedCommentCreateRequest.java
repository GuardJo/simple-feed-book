package com.guardjo.feedbook.controller.request;

import org.springframework.util.StringUtils;

public record FeedCommentCreateRequest(
	String content
) {
	public boolean isValid() {
		return StringUtils.hasText(this.content);
	}
}
