package com.guardjo.feedbook.service;

import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.WrongPasswordException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.AccountCache;
import com.guardjo.feedbook.repository.AccountRepository;
import com.guardjo.feedbook.repository.cache.AccountAccessInfoRepository;
import com.guardjo.feedbook.repository.cache.AccountCacheRepository;
import com.guardjo.feedbook.util.JwtProvider;
import com.guardjo.feedbook.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountCacheRepository accountCacheRepository;
    @Mock
    private AccountAccessInfoRepository accessInfoRepository;
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

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.empty());
        given(accountRepository.findByUsername(eq(username))).willReturn(Optional.of(account));
        given(passwordEncoder.matches(eq(password), eq(account.getPassword()))).willReturn(true);
        given(jwtProvider.createToken(eq(account))).willReturn(expected);

        String actual = accountService.login(username, password);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);

        then(accountCacheRepository).should().findById(eq(username));
        then(accountRepository).should().findByUsername(eq(username));
        then(passwordEncoder).should().matches(eq(password), eq(account.getPassword()));
        then(jwtProvider).should().createToken(eq(account));
    }

    @DisplayName("회원 로그인 테스트 : 아이디가 존재하지 않는 경우")
    @Test
    void test_login_not_found_account() {
        Account expected = TestDataGenerator.account(1, "test");
        String username = expected.getUsername();
        String password = expected.getPassword();

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.empty());
        given(accountRepository.findByUsername(eq(username))).willReturn(Optional.empty());

        assertThatCode(() -> accountService.login(username, password)).isInstanceOf(EntityNotFoundException.class);

        then(accountCacheRepository).should().findById(eq(username));
        then(accountRepository).should().findByUsername(eq(username));
    }

    @DisplayName("회원 로그인 테스트 : 비밀번호가 올바르지 않은 경우")
    @Test
    void test_login_wrong_password() {
        Account expected = TestDataGenerator.account(1, "test");
        String username = expected.getUsername();
        String password = expected.getPassword();

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.empty());
        given(accountRepository.findByUsername(eq(username))).willReturn(Optional.of(expected));
        given(passwordEncoder.matches(eq(password), eq(expected.getPassword()))).willReturn(false);

        assertThatCode(() -> accountService.login(username, password)).isInstanceOf(WrongPasswordException.class);

        then(accountCacheRepository).should().findById(eq(username));
        then(accountRepository).should().findByUsername(eq(username));
        then(passwordEncoder).should().matches(eq(password), eq(expected.getPassword()));
    }

    @DisplayName("회원 조회 테스트 : 정상")
    @Test
    void test_findAccount() {
        Account expected = TestDataGenerator.account(1, "test");
        String username = expected.getUsername();

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.empty());
        given(accountRepository.findByUsername(eq(username))).willReturn(Optional.of(expected));

        Account actual = accountService.findAccount(username);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsername()).isEqualTo(username);

        then(accountCacheRepository).should().findById(eq(username));
        then(accountRepository).should().findByUsername(eq(username));
    }

    @DisplayName("회원 조회 테스트 : 캐싱 조회")
    @Test
    void test_findAccount_Cache() {
        Account expected = TestDataGenerator.account(1, "test");
        String username = expected.getUsername();
        AccountCache accountCache = AccountCache.builder()
                .id(expected.getUsername())
                .account(expected)
                .build();

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.of(accountCache));

        Account actual = accountService.findAccount(username);

        assertThat(actual).isNotNull();
        assertThat(actual.getUsername()).isEqualTo(username);

        then(accountCacheRepository).should().findById(eq(username));
    }

    @DisplayName("회원 조회 테스트 : 조회 실패")
    @Test
    void test_findAccount_NotFoundEntity() {
        String username = "wrong";

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.empty());
        given(accountRepository.findByUsername(eq(username))).willReturn(Optional.empty());

        assertThatCode(() -> accountService.findAccount(username))
                .isInstanceOf(EntityNotFoundException.class);

        then(accountCacheRepository).should().findById(eq(username));
        then(accountRepository).should().findByUsername(eq(username));
    }

    @DisplayName("회원 조회 테스트 : 캐시 조회 실패")
    @Test
    void test_findAccount_NotFoundEntity_Cache() {
        Account expected = TestDataGenerator.account(1L, "tester");
        String username = expected.getUsername();

        ArgumentCaptor<AccountCache> argumentCaptor = ArgumentCaptor.forClass(AccountCache.class);

        given(accountCacheRepository.findById(eq(username))).willReturn(Optional.empty());
        given(accountRepository.findByUsername(eq(username))).willReturn(Optional.of(expected));
        given(accountCacheRepository.save(argumentCaptor.capture())).willReturn(mock(AccountCache.class));

        Account actual = accountService.findAccount(username);
        AccountCache cache = argumentCaptor.getValue();

        assertThat(actual).isNotNull();
        assertThat(actual.getUsername()).isEqualTo(username);
        assertThat(cache).isNotNull();
        assertThat(cache.getId()).isEqualTo(username);
        assertThat(cache.getAccount()).isEqualTo(expected);

        then(accountCacheRepository).should().findById(eq(username));
        then(accountRepository).should().findByUsername(eq(username));
        then(accountCacheRepository).should().save(any(AccountCache.class));
    }

    @DisplayName("로그아웃 요청 처리")
    @Test
    void test_logout() {
        AccountPrincipal principal = TestDataGenerator.accountPrincipal(1L, "Tester");

        willDoNothing().given(accessInfoRepository).deleteById(eq(principal.getUsername()));

        assertThatCode(() -> accountService.logout(principal))
                .doesNotThrowAnyException();

        then(accessInfoRepository).should().deleteById(eq(principal.getUsername()));
    }
}