package com.guardjo.feedbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.exception.DuplicateUsernameException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice(basePackageClasses = AccountController.class)
@Slf4j
public class AccountControllerHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = {
		IllegalArgumentException.class
	})
	public BaseResponse<String> handleBadRequest(Exception e) {
		log.warn("Exception : BadRequest, ", e);
		return BaseResponse.<String>builder()
			.status(HttpStatus.BAD_REQUEST.name())
			.body(e.getMessage())
			.build();
	}

	@ExceptionHandler(value = {
		DuplicateUsernameException.class
	})
	public BaseResponse<String> handleConflict(Exception e) {
		log.warn("Exception : Conflict, ", e);
		return BaseResponse.<String>builder()
			.status(HttpStatus.CONFLICT.name())
			.body(e.getMessage())
			.build();
	}
}
