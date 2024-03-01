package com.guardjo.feedbook.util;

import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;

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

	public static Feed feed(String title, Account account) {
		return Feed.builder()
			.title(title)
			.content("test content")
			.account(account)
			.build();
	}
}
