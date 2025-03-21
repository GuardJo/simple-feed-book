package com.guardjo.feedbook.util;

import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import com.guardjo.feedbook.repository.AlarmSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
     * @param accountId 알람 이벤트 수신 계정 식별키
     */
    public void sendAlarmUpdateEvent(Long accountId) {
        log.debug("Send Alarm Event, accountId = {}", accountId);
        AlarmSubscriber subscriber = alarmSubscriberRepository.getClient(accountId);

        try {
            subscriber.send("Update Alarm");
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
        } else {
            subscriber.onCompletion(() -> alarmSubscriberRepository.deleteClient(accountId));
            subscriber.onTimeout(() -> alarmSubscriberRepository.deleteClient(accountId));
            subscriber.onError((e) -> alarmSubscriberRepository.deleteClient(accountId));
        }

        return subscriber;
    }
}
