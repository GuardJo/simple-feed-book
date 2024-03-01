package com.guardjo.feedbook.controller.request;

import org.springframework.util.StringUtils;

public record FeedCreateRequest(
	String title,
	String content
) {
	public boolean isValid() {
		return StringUtils.hasText(title);
	}
}
