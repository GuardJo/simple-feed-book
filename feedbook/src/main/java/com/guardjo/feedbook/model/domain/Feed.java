package com.guardjo.feedbook.model.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
	private List<Account> favoriteAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
	@Builder.Default
	private List<FeedAlarm> feedAlarms = new ArrayList<>();

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
