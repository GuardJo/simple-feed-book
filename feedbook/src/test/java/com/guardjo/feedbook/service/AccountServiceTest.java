package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountService accountService;

	@DisplayName("신규 회원 저장 테스트 : 정상")
	@Test
	void test_createAccount() {
		String username = "test";
		String nickname = "tester";
		String password = "1234";
		String encodedPassword = "xxxx";

		ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

		given(accountRepository.existsByUsername(eq(username))).willReturn(false);
		given(passwordEncoder.encode(eq(password))).willReturn(encodedPassword);
		given(accountRepository.save(captor.capture())).willReturn(any(Account.class));

		assertThatCode(() -> accountService.createAccount(username, nickname, password)).doesNotThrowAnyException();

		Account actual = captor.getValue();

		assertThat(actual.getUsername()).isEqualTo(username);
		assertThat(actual.getNickname()).isEqualTo(nickname);
		assertThat(actual.getPassword()).isEqualTo(encodedPassword);

		then(accountRepository).should().existsByUsername(eq(username));
		then(passwordEncoder).should().encode(eq(password));
		then(accountRepository).should().save(any(Account.class));
	}

	@DisplayName("신규 회원 저장 테스트 : 이미 가입된 계정")
	@Test
	void test_createAccount_duplicateAccount() {
		String username = "test";
		String nickname = "tester";
		String password = "1234";

		given(accountRepository.existsByUsername(eq(username))).willReturn(true);

		assertThatCode(() -> accountService.createAccount(username, nickname, password)).isInstanceOf(DuplicateUsernameException.class);

		then(accountRepository).should().existsByUsername(eq(username));
	}
}