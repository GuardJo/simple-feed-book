package com.guardjo.feedbook.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.repository.FeedAlarmRepository;
import com.guardjo.feedbook.repository.FeedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedAlarmService {
	private final FeedAlarmRepository feedAlarmRepository;
	private final FeedRepository feedRepository;

	/**
	 * 주어진 인자를 기반으로 신규 알림을 저장한다.
	 * @param alarmType 알림 종류
	 * @param alarmArgs 알림 종류별 필요 인자
	 * @param feedId 알림이 생성될 피드
	 */
	@Async
	public void saveNewAlarm(AlarmType alarmType, AlarmArgs alarmArgs, long feedId) {
		log.debug("Create new FeedAlarm, type = {}, feedId = {}", alarmType, feedId);

		Feed targetFeed = feedRepository.getReferenceById(feedId);

		FeedAlarm newAlarm = FeedAlarm.builder()
			.feed(targetFeed)
			.alarmType(alarmType)
			.args(alarmArgs)
			.build();

		feedAlarmRepository.save(newAlarm);

		log.info("Created New FeedAlarm, type = {}, feedId = {}", alarmType, feedId);
	}
}
