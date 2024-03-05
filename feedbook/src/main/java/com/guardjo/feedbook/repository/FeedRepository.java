package com.guardjo.feedbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardjo.feedbook.model.domain.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
