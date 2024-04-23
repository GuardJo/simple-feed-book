package com.guardjo.feedbook.repository.querydsl;

import com.guardjo.feedbook.controller.response.FeedDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.QFeed;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class FeedDtoRepositoryImpl extends QuerydslRepositorySupport implements FeedDtoRepository {
    public FeedDtoRepositoryImpl() {
        super(Feed.class);
    }

    @Override
    public Page<FeedDto> findAllFeedDto(Pageable pageable, Account account) {
        QFeed qFeed = QFeed.feed;

        BooleanExpression isOwner = qFeed.account.eq(account);
        BooleanExpression isFavorite = qFeed.favoriteAccounts.contains(account);

        JPQLQuery<FeedDto> query = from(qFeed)
                .select(Projections.constructor(FeedDto.class,
                        qFeed.id,
                        qFeed.title,
                        qFeed.content,
                        qFeed.account.nickname,
                        isOwner,
                        isFavorite,
                        qFeed.favorites));

        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
