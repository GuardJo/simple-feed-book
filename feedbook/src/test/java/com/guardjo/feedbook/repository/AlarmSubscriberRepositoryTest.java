package com.guardjo.feedbook.repository;

import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AlarmSubscriberRepositoryTest {
    private final AlarmSubscriberRepository alarmSubscriberRepository = new AlarmSubscriberRepositoryImpl();

    @DisplayName("알람 구독 client 저장 및 조회")
    @Test
    void test_addClient_and_getClient() {
        Long accountId = 1L;

        alarmSubscriberRepository.addClient(accountId);

        AlarmSubscriber subscriber = alarmSubscriberRepository.getClient(accountId);

        assertThat(subscriber).isNotNull();
    }

    @DisplayName("알람 구독 client 제거")
    @Test
    void test_deleteClient() {
        Long accountId = 1L;

        alarmSubscriberRepository.addClient(accountId);
        alarmSubscriberRepository.deleteClient(accountId);

        AlarmSubscriber subscriber = alarmSubscriberRepository.getClient(accountId);

        assertThat(subscriber).isNull();
    }
}