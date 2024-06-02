package com.guardjo.feedbook.controller.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.FeedAlarm;

public record FeedAlarmPageDto(
	long totalSize,
	int pageNumber,
	List<FeedAlarmDto> feedAlarms
) {
	public static FeedAlarmPageDto from(Page<FeedAlarm> feedAlarms, Account account) {
		return new FeedAlarmPageDto(
			feedAlarms.getTotalElements(),
			feedAlarms.getNumber(),
			feedAlarms.getContent().stream()
				.map(feedAlarm -> FeedAlarmDto.from(feedAlarm, account))
				.toList()
		);
	}
}
