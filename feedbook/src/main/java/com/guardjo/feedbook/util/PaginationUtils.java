package com.guardjo.feedbook.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static Sort DEFAULT_SORT_BY_CREATED_AT_DESC = Sort.by(Sort.Order.desc("createdAt"));

    /**
     * <p>주어진 페이지 번호에 대한 Pageable 객체 반환<p>
     * <hr/>
     * <i>기본 정렬 : BaseEntity의 createdAt 기준 내림차 순 정렬</i>
     *
     * @param pageNumber 페이지 번호
     * @return Pageable
     * @see com.guardjo.feedbook.model.domain.BaseEntity
     */
    public static Pageable fromSortByCreatedAtDesc(int pageNumber) {
        return fromSortByCreatedAtDesc(pageNumber, DEFAULT_PAGE_SIZE);
    }

    /**
     * <p>주어진 페이지 번호 및 페이지 크기에 따른 Pageable 객체 반환<p>
     * <hr/>
     * <i>기본 정렬 : BaseEntity의 createdAt 기준 내림차 순 정렬</i>
     *
     * @param pageNumber 페이지 번호
     * @param pageSize   페이지 크기
     * @return Pageable
     * @see com.guardjo.feedbook.model.domain.BaseEntity
     */
    public static Pageable fromSortByCreatedAtDesc(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize, DEFAULT_SORT_BY_CREATED_AT_DESC);
    }
}
