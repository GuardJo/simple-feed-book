package com.guardjo.feedbook.controller;

import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.feedbook.controller.request.SignupRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;

	@PostMapping(UrlContext.SIGNUP_URL)
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
}
