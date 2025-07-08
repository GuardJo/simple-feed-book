package com.guardjo.feedbook.util;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.model.domain.*;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;

public class TestDataGenerator {
    private TestDataGenerator() {
    }

    public static Account account(String username) {
        return Account.builder()
                .username(username)
                .nickname("Tester")
                .password("1234")
                .build();
    }

    public static Account account(long id, String username) {
        return Account.builder()
                .id(id)
                .username(username)
                .nickname("Tester")
                .password("1234")
                .build();
    }

    public static Feed feed(String title, Account account) {
        return Feed.builder()
                .title(title)
                .content("test content")
                .account(account)
                .build();
    }

    public static Feed feed(long id, String title, String content, Account account) {
        return Feed.builder()
                .id(id)
                .title(title)
                .content(content)
                .account(account)
                .build();
    }

    public static FeedComment feedComment(String content, Feed feed, Account account) {
        return FeedComment.builder()
                .content(content)
                .feed(feed)
                .account(account)
                .build();
    }

    public static FeedComment feedComment(long id, String content, Feed feed, Account account) {
        return FeedComment.builder()
                .id(id)
                .content(content)
                .feed(feed)
                .account(account)
                .build();
    }

    public static FeedAlarm feedAlarm(AlarmType alarmType, AlarmArgs alarmArgs, Feed feed) {
        return FeedAlarm.builder()
                .alarmType(alarmType)
                .args(alarmArgs)
                .feed(feed)
                .build();
    }

    public static AccountPrincipal accountPrincipal(Long id, String username) {
        Account account = account(id, username);

        return AccountPrincipal.builder()
                .account(account)
                .build();
    }

    public static AccountAccessInfo accountAccessInfo(String username, String token) {
        return AccountAccessInfo.builder()
                .id(username)
                .token(token)
                .build();
    }
}
