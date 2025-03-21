package com.guardjo.feedbook.model.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class AlarmSubscriber extends SseEmitter {
    private final static long DEFAULT_TIMEOUT = 60L * 1000L;
    public AlarmSubscriber() {
        super(DEFAULT_TIMEOUT);
    }
}
