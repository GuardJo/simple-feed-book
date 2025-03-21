package com.guardjo.feedbook.util;

import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import com.guardjo.feedbook.repository.AlarmSubscriberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FeedNotificationUtilTest {
    @Mock
    private AlarmSubscriberRepository alarmSubscriberRepository;

    @InjectMocks
    private FeedNotificationUtil feedNotificationUtil;

    @DisplayName("알림 이벤트 발송")
    @Test
    void test_sendAlarmUpdateEvent() throws IOException {
        Long accountId = 1L;

        AlarmSubscriber expected = mock(AlarmSubscriber.class);

        given(alarmSubscriberRepository.getClient(eq(accountId))).willReturn(expected);
        willDoNothing().given(expected).send(any(String.class));

        assertThatCode(() -> feedNotificationUtil.sendAlarmUpdateEvent(accountId))
                .doesNotThrowAnyException();

        then(alarmSubscriberRepository).should().getClient(eq(accountId));
        then(expected).should().send(any(String.class));
    }

    @Test
    void test_subscribeFeedAlarm() {
        Long accountId = 1L;
        AlarmSubscriber expected = new AlarmSubscriber();

        given(alarmSubscriberRepository.getClient(eq(accountId))).willReturn(expected);

        AlarmSubscriber actual = feedNotificationUtil.subscribeFeedAlarm(accountId);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);

        then(alarmSubscriberRepository).should().getClient(eq(accountId));
    }
}