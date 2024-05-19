package com.guardjo.feedbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.guardjo.feedbook.model.domain.FeedComment;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
	Page<FeedComment> findAllByFeedId(Long feedId, Pageable pageable);
}
