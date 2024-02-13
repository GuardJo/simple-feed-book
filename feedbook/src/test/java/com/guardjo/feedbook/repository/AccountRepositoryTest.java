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
}