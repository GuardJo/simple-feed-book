package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.repository.FeedAlarmRepository;
import com.guardjo.feedbook.repository.FeedRepository;
import com.guardjo.feedbook.util.TestDataGenerator;

@ExtendWith(MockitoExtension.class)
class FeedAlarmServiceTest {
	@Mock
	private FeedAlarmRepository feedAlarmRepository;
	@Mock
	private FeedRepository feedRepository;

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

		assertThatCode(() -> feedAlarmService.saveNewAlarm(alarmType, alarmArgs, feedId))
			.doesNotThrowAnyException();

		FeedAlarm actual = argumentCaptor.getValue();
		assertThat(actual).isNotNull();
		assertThat(actual.getFeed()).isEqualTo(TEST_FEED);
		assertThat(actual.getAlarmType()).isEqualTo(alarmType);
		assertThat(actual.getArgs()).isEqualTo(alarmArgs);

		then(feedRepository).should().getReferenceById(eq(feedId));
		then(feedAlarmRepository).should().save(any(FeedAlarm.class));
	}

	private static Stream<Arguments> saveTestParams() {
		return Stream.of(
			Arguments.of(AlarmType.COMMENT, new AlarmArgs(TESTER.getId())),
			Arguments.of(AlarmType.FAVORITE, new AlarmArgs(TESTER.getId()))
		);
	}
}