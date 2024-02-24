package com.guardjo.feedbook.exception;

public class WrongPasswordException extends RuntimeException {
	public WrongPasswordException() {
		super("Wrong Password");
	}
}
