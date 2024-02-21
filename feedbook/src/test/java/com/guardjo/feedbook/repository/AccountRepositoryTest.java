package com.guardjo.feedbook.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.guardjo.feedbook.config.JpaConfig;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.util.TestDataGenerator;

@DataJpaTest
@Import(JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
	@Autowired
	private AccountRepository accountRepository;

	private final List<Account> testAccounts = new ArrayList<>();
	private final static int TEST_DATA_SIZE = 5;

	@BeforeEach
	void setUp() {
		for (int i = 0; i < TEST_DATA_SIZE; i++) {
			Account account = accountRepository.save(TestDataGenerator.account("test" + i));
			testAccounts.add(account);
		}
	}

	@AfterEach
	void tearDown() {
		testAccounts.clear();
	}

	@DisplayName("Account Entity 단일 조회 테스트")
	@Test
	void test_findById() {
		Account expected = testAccounts.get(0);
		long id = expected.getId();

		Account actual = accountRepository.findById(id).orElseThrow();

		assertThat(actual.getId()).isEqualTo(expected.getId());
	}

	@DisplayName("동일한 username으로 저장된 Account Entity 존재 여부 확인")
	@Test
	void test_existsUsername() {
		String existsName = testAccounts.get(0).getUsername();
		String notExistsName = "abcdefg";

		boolean exists = accountRepository.existsByUsername(existsName);
		boolean notExists = accountRepository.existsByUsername(notExistsName);

		assertThat(exists).isTrue();
		assertThat(notExists).isFalse();
	}

	@DisplayName("특정 username에 대한 Account Entity 단건 조회")
	@Test
	void test_findByUsername() {
		Account expected = testAccounts.get(0);

		Account actual = accountRepository.findByUsername(expected.getUsername()).orElseThrow();

		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
	}
}