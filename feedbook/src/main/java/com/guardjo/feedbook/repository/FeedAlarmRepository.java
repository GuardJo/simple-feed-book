package com.guardjo.feedbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guardjo.feedbook.model.domain.FeedAlarm;

public interface FeedAlarmRepository extends JpaRepository<FeedAlarm, Long> {
	@Query(
		"delete from FeedAlarm fa where fa.feed.account.id = :accountId"
	)
	@Modifying(clearAutomatically = true)
	void deleteAllByFeed_Account_Id(@Param("accountId") long accountId);

	Page<FeedAlarm> findAllByFeed_Account_Id(long accountId, Pageable pageable);
}
