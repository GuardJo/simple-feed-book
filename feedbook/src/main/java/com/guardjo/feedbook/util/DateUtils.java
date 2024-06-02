package com.guardjo.feedbook.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	/**
	 * Date -> String
	 * format : yyyy-MM-dd HH:mm
	 * @param date 형변환할 Date 객체
	 * @return 포멧에 맞게 변환된 String 객체
	 */
	public static String toString(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 주어진 Date와 현재 시간에 대한 차이를 문자열로 반환
	 * @param last 시간차를 구할 이전 시점
	 * @param next 시간차를 구할 이후 시점
	 * @return 시간차를 문자열로 반환
	 */
	public static String getTimelapse(Date last, Date next) {
		long target = last.getTime();
		long now = next.getTime();

		float diffSec = (float)(now - target) / (float)1000;
		String suffix = "초";

		if (diffSec >= 60 * 60 * 24) {
			diffSec /= 60 * 60 * 24;
			suffix = "일";
		} else if (diffSec >= 60 * 60) {
			diffSec /= 60 * 60;
			suffix = "시간";
		} else if (diffSec >= 60) {
			diffSec /= 60;
			suffix = "분";
		}

		return String.valueOf(Math.round(diffSec)) + suffix;
	}
}
