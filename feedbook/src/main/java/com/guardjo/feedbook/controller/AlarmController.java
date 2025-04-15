package com.guardjo.feedbook.controller;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.docs.AlarmApiDoc;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedAlarmPageDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.service.FeedAlarmService;
import com.guardjo.feedbook.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
public class AlarmController implements AlarmApiDoc {
    private final FeedAlarmService feedAlarmService;

    @GetMapping(UrlContext.ALARMS_URL)
    @Override
    public BaseResponse<FeedAlarmPageDto> getAlarms(@AuthenticationPrincipal AccountPrincipal principal,
                                                    @RequestParam("page") int pageNumber,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize) {
        Account account = principal.getAccount();
        log.info("GET : " + UrlContext.ALARMS_URL + " accountId = {}", account.getId());

        Pageable pageable = PaginationUtils.fromSortByCreatedAtDesc(pageNumber, pageSize);
        FeedAlarmPageDto feedAlarmPageDto = feedAlarmService.findAllFeedAlarmByAccount(account, pageable);

        return BaseResponse.<FeedAlarmPageDto>builder()
                .status(HttpStatus.OK.name())
                .body(feedAlarmPageDto)
                .build();
    }

    @DeleteMapping(UrlContext.ALARMS_URL)
    @Override
    public BaseResponse<String> deleteAllAlarms(@AuthenticationPrincipal AccountPrincipal principal) {
        Account account = principal.getAccount();
        log.info("DELETE : " + UrlContext.ALARMS_URL + " accountId = {}", account.getId());

        feedAlarmService.deleteAllFeedAlarmByAccount(account);

        return BaseResponse.defaultSuccesses();
    }

    @GetMapping(value = UrlContext.ALARM_SUB_URL, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Override
    public SseEmitter connectAlarms(@AuthenticationPrincipal AccountPrincipal principal) {
        Account account = principal.getAccount();

        log.info("GET : " + UrlContext.ALARM_SUB_URL + " accountId = {}", account.getId());

        return feedAlarmService.connectAlarmSubscriber(account.getId());
    }
}
