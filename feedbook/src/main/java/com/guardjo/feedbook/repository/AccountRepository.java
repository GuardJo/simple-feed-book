package com.guardjo.feedbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardjo.feedbook.model.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsByUsername(String username);

	Optional<Account> findByUsername(String username);
}
