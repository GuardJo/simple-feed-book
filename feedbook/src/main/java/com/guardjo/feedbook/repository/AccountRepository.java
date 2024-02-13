package com.guardjo.feedbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardjo.feedbook.model.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
