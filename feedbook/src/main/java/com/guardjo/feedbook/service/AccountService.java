package com.guardjo.feedbook.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.WrongPasswordException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService {
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 주어진 인자를 기반으로 신규 유저를 생성한다.
	 * <hr>
	 * <i>이미 존재하는 유저일 경우, DuplicateUsernameException 발생</i>
	 * @param username 가입 아이디
	 * @param nickname 유저 닉네임
	 * @param password 가입 비밀번호
	 */
	public void createAccount(String username, String nickname, String password) {
		if (accountRepository.existsByUsername(username)) {
			log.warn("Duplicate username, username = {}", username);
			throw new DuplicateUsernameException(username);
		}

		Account newAccount = Account.builder()
			.username(username)
			.nickname(nickname)
			.password(passwordEncoder.encode(password))
			.build();

		accountRepository.save(newAccount);
	}

	/**
	 * 주어진 username, password 에 해당하는 Account Entity를 반환한다.
	 * @param username 사용자 아이디
	 * @param password 사용자 비밀번호 (plain)
	 * @return 해당하는 계정 Entity,
	 * @throws com.guardjo.feedbook.exception.EntityNotFoundException 해당하는 아이디의 계정이 없을 때
	 * @throws com.guardjo.feedbook.exception.WrongPasswordException 해당하는 계정의 비밀번호가 올바르지 않을 때
	 */
	@Transactional(readOnly = true)
	public Account login(String username, String password) {
		Account account = accountRepository.findByUsername(username)
			.orElseThrow(() -> new EntityNotFoundException(Account.class, "username", username));

		if (passwordEncoder.matches(password, account.getPassword())) {
			log.info("Successes Login, nickname = {}", account.getNickname());
			return account;
		} else {
			log.warn("Invalid Password");
			throw new WrongPasswordException();
		}
	}
}
