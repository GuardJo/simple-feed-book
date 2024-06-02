package com.guardjo.feedbook.util;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DateUtilsTest {
	private final static Calendar TEST_CAL = Calendar.getInstance();

	@BeforeEach
	void setUp() {
		TEST_CAL.set(2024, Calendar.JUNE, 1, 14, 44);

	}

	@DisplayName("Date 객체 문자열 변환")
	@Test
	void test_toString() {
		Date date = TEST_CAL.getTime();
		String expected = "2024-06-01 14:44";

		String actual = DateUtils.toString(date);

		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("현재시간과의 경과 시간 조회")
	@ParameterizedTest
	@MethodSource("initTimeParams")
	void test_getTimelapse(Date last, Date next, String expected) {
		String actual = DateUtils.getTimelapse(last, next);

		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> initTimeParams() {
		Date base = TEST_CAL.getTime();

		return Stream.of(
			Arguments.of(base, new Date(base.getTime() + (10 * 1000)), "10초"),
			Arguments.of(base, new Date(base.getTime() + (1800 * 1000)), "30분"),
			Arguments.of(base, new Date(base.getTime() + (3 * 60 * 60 * 1000)), "3시간"),
			Arguments.of(base, new Date(base.getTime() + (25 * 60 * 60 * 1000)), "1일")
		);
	}
}