package com.guardjo.feedbook.controller.docs;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedAlarmPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림", description = "알림 관련 API")
public interface AlarmApiDoc {
    @Operation(summary = "알림 목록 조회", description = "페이지네이션 요청에 따른 현재 계정의 알림 목록을 반환한다.")
    BaseResponse<FeedAlarmPageDto> getAlarms(AccountPrincipal principal, int pageNumber, int pageSize);

    @Operation(summary = "알림 전체 삭제", description = "현재 계정의 알림들을 모두 삭제한다.")
    BaseResponse<String> deleteAllAlarms(AccountPrincipal principal);

    @Operation(summary = "알림 SSE 연결", description = "현재 계정의 생성되는 알림들을 SSE로 수신받도록 연결한다.",
            responses = {
                    @ApiResponse(content = {
                            @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)
                    })
            }
    )
    SseEmitter connectAlarms(AccountPrincipal principal);
}
