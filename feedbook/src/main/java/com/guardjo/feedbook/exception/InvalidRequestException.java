package com.guardjo.feedbook.exception;

public class InvalidRequestException extends RuntimeException {
	public InvalidRequestException() {
		super("Invalid Request");
	}
}
