package com.guardjo.feedbook.controller.response;

import java.util.List;

public record FeedPageDto(
        List<FeedDto> feeds,
        long totalPage
) {
}
