package com.guardjo.feedbook.repository;

import com.guardjo.feedbook.config.JpaConfig;
import com.guardjo.feedbook.controller.response.FeedDto;
import com.guardjo.feedbook.model.domain.Account;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedRepositoryTest {
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private AccountRepository accountRepository;
    private final List<Feed> testFeeds = new ArrayList<>();

    private final static int TEST_DATA_SIZE = 5;

    @BeforeEach
    void setUp() {
        Account account = accountRepository.save(TestDataGenerator.account("test123"));
        Account account2 = accountRepository.save(TestDataGenerator.account("test222"));

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            Account tester = account;
            if (i == 0) {
                tester = account2;
            }
            Feed feed = TestDataGenerator.feed("test_" + i, tester);

            feed.setFavorites(1);
            feed.getFavoriteAccounts().add(account2);
            testFeeds.add(feedRepository.save(feed));
        }
    }

    @AfterEach
    void tearDown() {
        testFeeds.clear();
    }

    @DisplayName("식별키를 통한 Feed 단일 조회 테스트")
    @Test
    void test_findById() {
        Feed expected = testFeeds.get(0);

        Feed actual = feedRepository.findById(expected.getId()).orElseThrow();

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @DisplayName("사용자별 Feed 목록 조회 테스트")
    @ParameterizedTest
    @MethodSource("findAllByAccountTestArgs")
    void test_findAllByAccount(int feedIndex, int totalSize) {
        Account account = testFeeds.get(feedIndex).getAccount();

        Page<Feed> actual = feedRepository.findAllByAccount(Pageable.ofSize(10), account);

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalElements()).isEqualTo(totalSize);
    }

    @DisplayName("좋아요가 포함된 FeedDto Proection 조회 테스트")
    @Test
    void test_findAllFeedDto() {
        Account account = testFeeds.get(0).getAccount();
        Pageable pageable = Pageable.ofSize(10);
        Page<FeedDto> actual = feedRepository.findAllFeedDto(pageable, account);

        assertThat(actual).isNotNull();
        assertThat(actual.get().toList()).isNotNull();
        assertThat(actual.getTotalElements()).isEqualTo(TEST_DATA_SIZE);
    }

    private static Stream<Arguments> findAllByAccountTestArgs() {
        return Stream.of(
                Arguments.of(0, 1), // 0번에 해당하는 Feed는 1개
                Arguments.of(1, TEST_DATA_SIZE - 1)
        );
    }
}