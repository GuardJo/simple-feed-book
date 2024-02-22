package com.guardjo.feedbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.WrongPasswordException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.repository.AccountRepository;
import com.guardjo.feedbook.util.JwtProvider;
import com.guardjo.feedbook.util.TestDataGenerator;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private JwtProvider jwtProvider;

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

	@DisplayName("회원 로그인 테스트 : 정상")
	@Test
	void test_login() {
		Account account = TestDataGenerator.account(1, "test");
		String username = account.getUsername();
		String password = account.getPassword();
		String expected = "test-token";

		given(accountRepository.findByUsername(eq(username))).willReturn(Optional.of(account));
		given(passwordEncoder.matches(eq(password), eq(account.getPassword()))).willReturn(true);
		given(jwtProvider.createToken(eq(account.getUsername()))).willReturn(expected);

		String actual = accountService.login(username, password);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);

		then(accountRepository).should().findByUsername(eq(username));
		then(passwordEncoder).should().matches(eq(password), eq(account.getPassword()));
		then(jwtProvider).should().createToken(eq(account.getUsername()));
	}

	@DisplayName("회원 로그인 테스트 : 아이디가 존재하지 않는 경우")
	@Test
	void test_login_not_found_account() {
		Account expected = TestDataGenerator.account(1, "test");
		String username = expected.getUsername();
		String password = expected.getPassword();

		given(accountRepository.findByUsername(eq(username))).willReturn(Optional.empty());

		assertThatCode(() -> accountService.login(username, password)).isInstanceOf(EntityNotFoundException.class);

		then(accountRepository).should().findByUsername(eq(username));
	}

	@DisplayName("회원 로그인 테스트 : 비밀번호가 올바르지 않은 경우")
	@Test
	void test_login_wrong_password() {
		Account expected = TestDataGenerator.account(1, "test");
		String username = expected.getUsername();
		String password = expected.getPassword();

		given(accountRepository.findByUsername(eq(username))).willReturn(Optional.of(expected));
		given(passwordEncoder.matches(eq(password), eq(expected.getPassword()))).willReturn(false);

		assertThatCode(() -> accountService.login(username, password)).isInstanceOf(WrongPasswordException.class);

		then(accountRepository).should().findByUsername(eq(username));
		then(passwordEncoder).should().matches(eq(password), eq(expected.getPassword()));
	}
}