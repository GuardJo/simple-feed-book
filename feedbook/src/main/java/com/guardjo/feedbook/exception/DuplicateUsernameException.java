package com.guardjo.feedbook.exception;

public class DuplicateUsernameException extends RuntimeException {
	public DuplicateUsernameException(String username) {
		super(String.format("Already Registration Username, username = %s", username));
	}
}
