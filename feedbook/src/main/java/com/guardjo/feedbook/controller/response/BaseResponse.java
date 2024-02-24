package com.guardjo.feedbook.controller.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BaseResponse<T> {
	private String status;
	private T body;

	public static BaseResponse<String> defaultSuccesses() {
		return BaseResponse.<String>builder()
			.status(HttpStatus.OK.name())
			.body("SUCCESSES")
			.build();
	}
}
