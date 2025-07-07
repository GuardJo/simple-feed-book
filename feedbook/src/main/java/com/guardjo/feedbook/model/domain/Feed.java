package com.guardjo.feedbook.model.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feed")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder(toBuilder = true)
@ToString
public class Feed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String title;
    @Setter
    private String content;
    @Setter
    private int favorites;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "account_favorite_feed",
            joinColumns = @JoinColumn(name = "feed_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @Builder.Default
    @ToString.Exclude
    private List<Account> favoriteAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    @Builder.Default
    @ToString.Exclude
    private List<FeedAlarm> feedAlarms = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    @Builder.Default
    @ToString.Exclude
    private List<FeedComment> feedComments = new ArrayList<>();

    /**
     * 해당 계정의 favorite을 추가한다.
     *
     * @param account
     */
    public void addFavorite(Account account) {
        this.favoriteAccounts.add(account);
        this.favorites++;
    }

    /**
     * 해당 계정의 favorite을 제거한다.
     *
     * @param account
     */
    public void subtractFavorite(Account account) {
        this.favoriteAccounts.remove(account);
        this.favorites--;
    }
}
