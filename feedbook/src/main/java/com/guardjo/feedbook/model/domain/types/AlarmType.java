package com.guardjo.feedbook.model.domain.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
	FAVORITE("%s님 외 %d명이 %s 게시글을 좋아합니다."),
	COMMENT("%s님이 %s 게시글에 신규 댓글을 등록하였습니다.");

	private final String format;
}
