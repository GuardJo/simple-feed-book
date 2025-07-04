package com.guardjo.feedbook.repository;

import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AlarmSubscriberRepositoryImpl implements AlarmSubscriberRepository {
    private final Map<Long, AlarmSubscriber> subscribeStorage = new ConcurrentHashMap<>();

    @Override
    public AlarmSubscriber getClient(Long accountId) {
        return subscribeStorage.computeIfAbsent(accountId, this::generateSubscriber);
    }

    @Override
    public AlarmSubscriber addClient(Long accountId) {
        return subscribeStorage.put(accountId, new AlarmSubscriber());
    }

    @Override
    public void deleteClient(Long accountId) {
        AlarmSubscriber subscriber = subscribeStorage.remove(accountId);

        if (Objects.nonNull(subscriber)) {
            subscriber.complete();
        }
    }

    /*
    알림 구독 객체 신규 생성
     */
    private AlarmSubscriber generateSubscriber(Long accountId) {
        AlarmSubscriber alarmSubscriber = new AlarmSubscriber();
        alarmSubscriber.onCompletion(() -> this.deleteClient(accountId));
        alarmSubscriber.onTimeout(() -> this.deleteClient(accountId));
        alarmSubscriber.onError((e) -> this.deleteClient(accountId));

        return alarmSubscriber;
    }
}
