package com.guardjo.feedbook.repository.querydsl;

import com.guardjo.feedbook.controller.response.FeedDto;
import com.guardjo.feedbook.model.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedDtoRepository {
    Page<FeedDto> findAllFeedDto(Pageable pageable, Account account);
}
