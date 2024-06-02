package com.guardjo.feedbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.guardjo.feedbook.model.domain.FeedAlarm;

public interface FeedAlarmRepository extends JpaRepository<FeedAlarm, Long> {
	void deleteAllByFeed_Account_Id(long accountId);

	Page<FeedAlarm> findAllByFeed_Account_Id(long accountId, Pageable pageable);
}
