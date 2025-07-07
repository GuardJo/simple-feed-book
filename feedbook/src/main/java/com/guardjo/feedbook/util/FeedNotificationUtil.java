package com.guardjo.feedbook.util;

import com.guardjo.feedbook.controller.response.FeedAlarmDto;
import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.repository.AlarmSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedNotificationUtil {
    private final AlarmSubscriberRepository alarmSubscriberRepository;

    /**
     * 알람 이벤트 발송
     *
     * @param feedAlarm 피드 알림 Entity
     */
    public void sendAlarmUpdateEvent(FeedAlarm feedAlarm) {
        Long accountId = feedAlarm.getFeed().getAccount().getId();
        log.debug("Send Alarm Event, accountId = {}, feedId = {}", accountId, feedAlarm.getFeed().getId());
        AlarmSubscriber subscriber = alarmSubscriberRepository.getClient(accountId);

        FeedAlarmDto feedAlarmDto = FeedAlarmDto.from(feedAlarm, feedAlarm.getArgs().accountName());

        try {
            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                    .name("message")
                    .data(feedAlarmDto.alarmText())
                    .reconnectTime(30_000);
            subscriber.send(eventBuilder);
        } catch (IOException e) {
            log.warn("Failed to send Alarm Event, accountId = {}", accountId);
            alarmSubscriberRepository.deleteClient(accountId);
        }
    }

    /**
     * 알람 이벤트 연결
     *
     * @param accountId 알람 이벤트 수신 계정 식별키
     * @return AlarmSubscriber
     * @see org.springframework.web.servlet.mvc.method.annotation.SseEmitter
     */
    public AlarmSubscriber subscribeFeedAlarm(Long accountId) {
        log.debug("Connect AlarmSubscriber, accountId = {}", accountId);

        AlarmSubscriber subscriber = alarmSubscriberRepository.getClient(accountId);

        if (Objects.isNull(subscriber)) {
            subscriber = alarmSubscriberRepository.addClient(accountId);
        }

        return subscriber;
    }
}
