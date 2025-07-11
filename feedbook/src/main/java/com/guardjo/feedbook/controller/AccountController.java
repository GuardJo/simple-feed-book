package com.guardjo.feedbook.controller;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.controller.docs.AccountApiDoc;
import com.guardjo.feedbook.controller.request.LoginRequest;
import com.guardjo.feedbook.controller.request.SignupRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.exception.CustomAuthenticationException;
import com.guardjo.feedbook.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequiredArgsConstructor
public class AccountController implements AccountApiDoc {
    private final AccountService accountService;

    @PostMapping(UrlContext.SIGNUP_URL)
    @Override
    public BaseResponse<String> signup(@RequestBody SignupRequest signupRequest) {
        log.info("POST : {}, username = {}", UrlContext.SIGNUP_URL, signupRequest.username());

        String username = signupRequest.username();
        String nickname = signupRequest.nickname();
        String password = signupRequest.password();

        if (Objects.isNull(username) || Objects.isNull(nickname) || Objects.isNull(password)) {
            throw new IllegalArgumentException("Signup Data is Empty!");
        }

        accountService.createAccount(signupRequest.username(), signupRequest.nickname(), signupRequest.password());

        return BaseResponse.defaultSuccesses();
    }

    @PostMapping(UrlContext.LOGIN_URL)
    @Override
    public BaseResponse<String> login(@RequestBody LoginRequest loginRequest) {
        log.info("POST : {}, username = {}", UrlContext.LOGIN_URL, loginRequest.username());

        if (!loginRequest.validate()) {
            throw new IllegalArgumentException("Login Data is Wrong!");
        }

        String token = accountService.login(loginRequest.username(), loginRequest.password());

        return BaseResponse.<String>builder()
                .body(token)
                .status(HttpStatus.OK.name())
                .build();
    }

    @GetMapping(UrlContext.AUTH_URL)
    @Override
    public BaseResponse<String> authenticate(@AuthenticationPrincipal AccountPrincipal principal) {
        if (Objects.isNull(principal)) {
            throw new CustomAuthenticationException("사용자 정보를 찾을 수 없습니다.");
        }

        return BaseResponse.defaultSuccesses();
    }

    @PostMapping(UrlContext.LOGOUT_URL)
    @Override
    public BaseResponse<String> logout(@AuthenticationPrincipal AccountPrincipal principal) {
        if (Objects.nonNull(principal)) {
            accountService.logout(principal);
        }

        return BaseResponse.defaultSuccesses();
    }
}
