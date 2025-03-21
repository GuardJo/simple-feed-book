package com.guardjo.feedbook.service;

import com.guardjo.feedbook.controller.response.FeedAlarmPageDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.repository.FeedAlarmRepository;
import com.guardjo.feedbook.repository.FeedRepository;
import com.guardjo.feedbook.util.FeedNotificationUtil;
import com.guardjo.feedbook.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class FeedAlarmServiceTest {
    @Mock
    private FeedAlarmRepository feedAlarmRepository;
    @Mock
    private FeedRepository feedRepository;
    @Mock
    private FeedNotificationUtil feedNotificationUtil;

    @InjectMocks
    private FeedAlarmService feedAlarmService;

    private final static Account TESTER = TestDataGenerator.account(1L, "Tester");
    private final static Feed TEST_FEED = TestDataGenerator.feed(1L, "Test Feed", "test", TESTER);

    @DisplayName("신규 알림 저장 테스트")
    @ParameterizedTest
    @MethodSource("saveTestParams")
    void test_saveNewAlarm(AlarmType alarmType, AlarmArgs alarmArgs) {
        long feedId = TEST_FEED.getId();

        ArgumentCaptor<FeedAlarm> argumentCaptor = ArgumentCaptor.forClass(FeedAlarm.class);
        given(feedRepository.getReferenceById(eq(feedId))).willReturn(TEST_FEED);
        given(feedAlarmRepository.save(argumentCaptor.capture())).willReturn(mock(FeedAlarm.class));
        willDoNothing().given(feedNotificationUtil).sendAlarmUpdateEvent(eq(alarmArgs.accountId()));

        assertThatCode(() -> feedAlarmService.saveNewAlarm(alarmType, alarmArgs, feedId))
                .doesNotThrowAnyException();

        FeedAlarm actual = argumentCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getFeed()).isEqualTo(TEST_FEED);
        assertThat(actual.getAlarmType()).isEqualTo(alarmType);
        assertThat(actual.getArgs()).isEqualTo(alarmArgs);

        then(feedRepository).should().getReferenceById(eq(feedId));
        then(feedAlarmRepository).should().save(any(FeedAlarm.class));
        then(feedNotificationUtil).should().sendAlarmUpdateEvent(eq(alarmArgs.accountId()));
    }

    @DisplayName("계정별 피드 알림 조회")
    @Test
    void test_findAllFeedAlarmByAccount() {
        Account account = TestDataGenerator.account(99L, "Tester222");
        Pageable pageable = PageRequest.of(0, 10);
        Page<FeedAlarm> expected = new PageImpl<>(List.of(TestDataGenerator.feedAlarm(AlarmType.COMMENT, new AlarmArgs(account.getId()), TEST_FEED)));

        given(feedAlarmRepository.findAllByFeed_Account_Id(eq(account.getId()), eq(pageable))).willReturn(expected);

        FeedAlarmPageDto actual = feedAlarmService.findAllFeedAlarmByAccount(account, pageable);

        assertThat(actual).isNotNull();
        assertThat(actual.pageNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actual.totalSize()).isEqualTo(expected.getTotalElements());
        assertThat(actual.feedAlarms().size()).isEqualTo(expected.getContent().size());
        assertThat(actual.feedAlarms().get(0).alarmText().contains(expected.getContent().get(0).getFeed().getTitle())).isTrue();

        then(feedAlarmRepository).should().findAllByFeed_Account_Id(eq(account.getId()), eq(pageable));

    }

    @DisplayName("계정별 피드 알림 전체 삭제")
    @Test
    void test_deleteAllFeedAlarmByAccount() {
        willDoNothing().given(feedAlarmRepository).deleteAllByFeed_Account_Id(eq(TESTER.getId()));

        assertThatCode(() -> feedAlarmService.deleteAllFeedAlarmByAccount(TESTER))
                .doesNotThrowAnyException();

        then(feedAlarmRepository).should().deleteAllByFeed_Account_Id(eq(TESTER.getId()));
    }

    @DisplayName("알림 이벤트 연결 조회")
    @Test
    void test_subscribeFeedAlarm() {
        AlarmSubscriber expected = new AlarmSubscriber();

        given(feedNotificationUtil.subscribeFeedAlarm(eq(TESTER.getId()))).willReturn(expected);

        AlarmSubscriber actual = feedAlarmService.connectAlarmSubscriber(TESTER.getId());

        then(feedNotificationUtil).should().subscribeFeedAlarm(eq(TESTER.getId()));

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> saveTestParams() {
        return Stream.of(
                Arguments.of(AlarmType.COMMENT, new AlarmArgs(TESTER.getId())),
                Arguments.of(AlarmType.FAVORITE, new AlarmArgs(TESTER.getId()))
        );
    }
}