package com.guardjo.feedbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.controller.response.FeedAlarmPageDto;
import com.guardjo.feedbook.model.domain.Account;
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

	/**
	 * 주어진 계정 식별키에 해당하는 피드 알림 목록을 반환한다.
	 *
	 * @param account 피드 알림을 조회할 계정
	 * @param pageable 페이지네이션 옵션
	 * @return 전체 알림 개수, 현재 페이지 수, 현재 페이지의 피드 알림 목록
	 */
	public FeedAlarmPageDto findAllFeedAlarmByAccount(Account account, Pageable pageable) {
		log.debug("Find FeedAlarm List, accountId = {}", account.getId());

		Page<FeedAlarm> feedAlarms = feedAlarmRepository.findAllByFeed_Account_Id(account.getId(), pageable);

		return FeedAlarmPageDto.from(feedAlarms, account);
	}
}
