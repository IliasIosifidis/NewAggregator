package io.github.iliasiosifidis.searchservice.consumer;

import java.time.Instant;

public record ArticleMessage(
        String source,
        String externalId,
        String title,
        String url,
        String summary,
        Instant publishedAt) {}
