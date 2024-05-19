package com.guardjo.feedbook.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	public static String toString(Date date) {
		return dateFormat.format(date);
	}
}
