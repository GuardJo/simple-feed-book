package com.guardjo.feedbook.repository;

import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmSubscriberRepository {
    /**
     * 알람 구독상태인 client를 반환한다.
     *
     * @param accountId 알람 구독상태인 계정 식별키
     * @return AlarmSubscriber
     * @see SseEmitter
     */
    AlarmSubscriber getClient(Long accountId);

    /**
     * 신규 알람 구독 client를 저장한다.
     *
     * @param accountId 알람 구독 계정 식별키
     */
    AlarmSubscriber addClient(Long accountId);

    /**
     * 알림 구독 client를 제거한다.
     *
     * @param accountId 알람 구독 제거 계정 식별키
     */
    void deleteClient(Long accountId);
}
