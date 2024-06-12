package com.guardjo.feedbook.config.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.guardjo.feedbook.exception.CustomAuthenticationException;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.service.AccountService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthManager implements AuthenticationManager {
	private final AccountService accountService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (String)authentication.getPrincipal();

		Account account = searchAccount(username);

		AccountPrincipal principal = AccountPrincipal.builder()
			.account(account)
			.build();

		return new UsernamePasswordAuthenticationToken(principal, principal, principal.getAuthorities());
	}

	private Account searchAccount(String username) {
		try {
			// TODO  매 요청 마다 조회 해오니, 추후 Redis등으로 캐성하여 조회하도록 개선하는 것이 좋을 듯함
			return accountService.findAccount(username);
		} catch (Exception e) {
			throw new CustomAuthenticationException(e.getMessage());
		}
	}
}
