package com.guardjo.feedbook.repository;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.repository.querydsl.FeedDtoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedDtoRepository {
    Page<Feed> findAllByAccount(Pageable pageable, Account account);
}
