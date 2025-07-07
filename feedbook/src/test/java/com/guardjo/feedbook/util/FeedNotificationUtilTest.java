package com.guardjo.feedbook.util;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.repository.AlarmSubscriberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
        Account testAccount = TestDataGenerator.account(accountId, "Tester");
        Feed testFeed = TestDataGenerator.feed("Test", testAccount);
        FeedAlarm testAlarm = TestDataGenerator.feedAlarm(AlarmType.COMMENT, new AlarmArgs(accountId, testAccount.getNickname()), testFeed);

        AlarmSubscriber expected = mock(AlarmSubscriber.class);

        given(alarmSubscriberRepository.getClient(eq(accountId))).willReturn(expected);
        willDoNothing().given(expected).send(any(SseEmitter.SseEventBuilder.class));

        assertThatCode(() -> feedNotificationUtil.sendAlarmUpdateEvent(testAlarm))
                .doesNotThrowAnyException();

        then(alarmSubscriberRepository).should().getClient(eq(accountId));
        then(expected).should().send(any(SseEmitter.SseEventBuilder.class));
    }

    @DisplayName("알림 이벤트 스트림 연결")
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