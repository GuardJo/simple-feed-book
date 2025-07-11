package com.guardjo.feedbook.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlContext {
    public final static String SIGNUP_URL = "/api/signup";
    public final static String LOGIN_URL = "/api/login";
    public final static String AUTH_URL = "/api/auth";
    public final static String LOGOUT_URL = "/api/logout";
    public final static String FEEDS_URL = "/api/feeds";
    public final static String MY_FEEDS_URL = "/api/feeds/me";
    public final static String FAVORITE_FEEDS_URL = "/api/feeds/favorites";
    public final static String FEED_COMMENTS_URL = "/api/feeds/{feedId}/comments";
    public final static String ALARMS_URL = "/api/accounts/alarms";
    public final static String ALARM_SUB_URL = "/api/accounts/alarms/sub";
}
