package com.guardjo.feedbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardjo.feedbook.model.domain.FeedComment;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
	List<FeedComment> findByFeedId(Long feedId);
}
