package com.guardjo.feedbook.controller.request;

import java.util.Objects;

public record LoginRequest(
	String username,
	String password
) {
	public boolean validate() {
		return !(Objects.isNull(username) || Objects.isNull(password));
	}
}
