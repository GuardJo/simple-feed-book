package com.guardjo.feedbook.controller.docs;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.FeedCommentCreateRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedCommentPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "피드 댓글", description = "피드 댓글 관련 API")
public interface FeedCommentApiDoc {
    @Operation(summary = "신규 댓글 저장", description = "특정 피드에 신규 댓글 정보를 저장한다.")
    BaseResponse<String> saveFeedComment(FeedCommentCreateRequest request, long feedId, AccountPrincipal principal);

    @Operation(summary = "피드 댓글 목록 조회", description = "특정 피드에 등록된 댓글 목록을 반환한다.")
    BaseResponse<FeedCommentPageDto> getFeedComment(long feedId, int pageNumber, int pageSize, AccountPrincipal principal);
}
