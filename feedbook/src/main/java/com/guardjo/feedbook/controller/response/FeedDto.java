package com.guardjo.feedbook.controller.response;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;

public record FeedDto(
        long id,
        String title,
        String content,
        String author,
        boolean isOwner,
        boolean isFavorite,
        int totalFavorites
) {
    public static FeedDto from(Feed feed, Account principal) {
        Account owner = feed.getAccount();

        return new FeedDto(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                owner.getNickname(),
                owner.equals(principal),
                feed.getFavoriteAccounts().contains(principal),
                feed.getFavorites()
        );
    }
}
