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

	public static Account account(long id, String username) {
		return Account.builder()
			.id(id)
			.username(username)
			.nickname("Tester")
			.password("1234")
			.build();
	}
}
