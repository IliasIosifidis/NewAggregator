package io.github.iliasiosifidis;

import java.time.Instant;

public record ArticleMessage(
        String source,
        String externalId,
        String title,
        String url,
        String summary,
        Instant publishedAt) {}
