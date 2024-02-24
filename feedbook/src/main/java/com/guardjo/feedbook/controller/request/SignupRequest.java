package com.guardjo.feedbook.controller.request;

public record SignupRequest(
	String username,
	String nickname,
	String password
) {
}
