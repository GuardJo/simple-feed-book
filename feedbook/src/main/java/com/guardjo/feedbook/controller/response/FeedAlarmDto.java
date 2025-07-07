package com.guardjo.feedbook.controller.response;

import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.util.DateUtils;

import java.util.Date;

public record FeedAlarmDto(
        String alarmText,
        String alarmTime
) {
    public static FeedAlarmDto from(FeedAlarm feedAlarm, String targetName) {
        Feed targetFeed = feedAlarm.getFeed();
        String timelapse = DateUtils.getTimelapse(feedAlarm.getModifiedAt(), new Date());

        return switch (feedAlarm.getAlarmType()) {
            case FAVORITE -> new FeedAlarmDto(
                    String.format(AlarmType.FAVORITE.getFormat(), targetName, targetFeed.getFavorites(), targetFeed.getTitle()),
                    timelapse
            );
            case COMMENT -> new FeedAlarmDto(
                    String.format(AlarmType.COMMENT.getFormat(), targetName, targetFeed.getTitle()),
                    timelapse
            );
        };
    }
}
