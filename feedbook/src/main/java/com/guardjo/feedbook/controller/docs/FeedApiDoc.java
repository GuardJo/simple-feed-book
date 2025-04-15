package com.guardjo.feedbook.controller.docs;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.FeedCreateRequest;
import com.guardjo.feedbook.controller.request.FeedModifyRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "피드", description = "피드 관련 API")
public interface FeedApiDoc {
    @Operation(summary = "피드 생성", description = "주어진 데이터를 기반으로 신규 피드를 저장한다.")
    BaseResponse<String> createFeed(FeedCreateRequest request, AccountPrincipal principal);

    @Operation(summary = "피드 목록 조회", description = "현재 저장된 피드 목록을 조회한다.")
    BaseResponse<FeedPageDto> getFeedPage(AccountPrincipal principal, int pageNumber, int pageSize);

    @Operation(summary = "자신의 피드 목록 조회", description = "현재 계정이 저장한 피드 목록을 조회한다.")
    BaseResponse<FeedPageDto> getMyFeedPage(AccountPrincipal principal, int pageNumber, int pageSize);

    @Operation(summary = "피드 갱신", description = "특정 피드의 데이터를 수정한다.")
    BaseResponse<String> updateFeed(FeedModifyRequest feedModifyRequest, AccountPrincipal principal);

    @Operation(summary = "피드 삭제", description = "특정 피드를 삭제한다.")
    BaseResponse<String> deleteFeed(long feedId, AccountPrincipal principal);

    @Operation(summary = "피드 즐겨찾기 갱신", description = "특정 피드에 대한 즐겨찾기 여부를 갱신한다.")
    BaseResponse<String> updateFavoriteFeed(long feedId, AccountPrincipal principal);
}
