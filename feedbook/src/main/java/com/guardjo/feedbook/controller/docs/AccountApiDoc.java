package com.guardjo.feedbook.controller.docs;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.request.LoginRequest;
import com.guardjo.feedbook.controller.request.SignupRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원", description = "회원 관련 API")
public interface AccountApiDoc {
    @Operation(summary = "회원가입", description = "신규 회원 정보를 저장한다.")
    BaseResponse<String> signup(SignupRequest signupRequest);

    @Operation(summary = "로그인", description = "저장된 회원 정보를 기반으로 Access Token을 반환한다.")
    BaseResponse<String> login(LoginRequest loginRequest);

    @Operation(summary = "사용자 검증", description = "로그인한 사용자의 토큰 정보를 검증한다.")
    BaseResponse<String> authenticate(AccountPrincipal principal);
}
