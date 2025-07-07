package com.guardjo.feedbook.controller.response;

import com.guardjo.feedbook.model.domain.FeedAlarm;
import org.springframework.data.domain.Page;

import java.util.List;

public record FeedAlarmPageDto(
        long totalSize,
        int pageNumber,
        List<FeedAlarmDto> feedAlarms
) {
    public static FeedAlarmPageDto from(Page<FeedAlarm> feedAlarms) {
        return new FeedAlarmPageDto(
                feedAlarms.getTotalElements(),
                feedAlarms.getNumber(),
                feedAlarms.getContent().stream()
                        .map(feedAlarm -> FeedAlarmDto.from(feedAlarm, feedAlarm.getArgs().accountName()))
                        .toList()
        );
    }
}
