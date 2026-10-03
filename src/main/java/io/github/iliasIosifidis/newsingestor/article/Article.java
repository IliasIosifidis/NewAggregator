package io.github.iliasIosifidis.newsingestor.article;

import java.time.Instant;

public record Article(
        String source,
        String externalId,
        String title,
        String url,
        String summary,
        Instant publishedAt
) {}
