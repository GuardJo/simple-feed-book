package com.guardjo.feedbook.repository;

import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AlarmSubscriberRepositoryImpl implements AlarmSubscriberRepository {
    private final Map<Long, AlarmSubscriber> subscribeStorage = new ConcurrentHashMap<>();

    @Override
    public AlarmSubscriber getClient(Long accountId) {
        return subscribeStorage.get(accountId);
    }

    @Override
    public AlarmSubscriber addClient(Long accountId) {
        return subscribeStorage.put(accountId, new AlarmSubscriber());
    }

    @Override
    public void deleteClient(Long accountId) {
        AlarmSubscriber subscriber = subscribeStorage.remove(accountId);
        subscriber.complete();
    }
}
