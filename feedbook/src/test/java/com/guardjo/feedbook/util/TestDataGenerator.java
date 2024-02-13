package com.guardjo.feedbook.util;

import com.guardjo.feedbook.model.domain.Account;

public class TestDataGenerator {
	private TestDataGenerator() {
	}

	public static Account account(String username) {
		return Account.builder()
			.username(username)
			.nickname("Tester")
			.password("1234")
			.build();
	}
}
