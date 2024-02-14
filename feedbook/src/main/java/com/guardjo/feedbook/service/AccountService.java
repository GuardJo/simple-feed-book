package com.guardjo.feedbook.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardjo.feedbook.exception.DuplicateUsernameException;
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
}
