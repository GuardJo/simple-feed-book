package com.guardjo.feedbook.controller.request;

import java.util.Objects;

import org.springframework.util.StringUtils;

public record FeedModifyRequest(
	Long feedId,
	String title,
	String content
) {
	public boolean isValid() {
		return !Objects.isNull(feedId) && StringUtils.hasText(content);
	}
}
