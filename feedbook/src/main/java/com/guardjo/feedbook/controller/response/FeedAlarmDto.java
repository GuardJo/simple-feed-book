package com.guardjo.feedbook.controller.response;

import java.util.Date;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.util.DateUtils;

public record FeedAlarmDto(
	String alarmText,
	String alarmTime
) {
	public static FeedAlarmDto from(FeedAlarm feedAlarm, Account account) {
		Feed targetFeed = feedAlarm.getFeed();
		String targetAccountName = account.getNickname();
		String timelapse = DateUtils.getTimelapse(feedAlarm.getModifiedAt(), new Date());

		return switch (feedAlarm.getAlarmType()) {
			case FAVORITE -> new FeedAlarmDto(
				String.format(AlarmType.FAVORITE.getFormat(), targetAccountName, targetFeed.getFavorites(), targetFeed.getTitle()),
				timelapse
			);
			case COMMENT -> new FeedAlarmDto(
				String.format(AlarmType.COMMENT.getFormat(), targetAccountName, targetFeed.getTitle()),
				timelapse
			);
		};
	}
}
