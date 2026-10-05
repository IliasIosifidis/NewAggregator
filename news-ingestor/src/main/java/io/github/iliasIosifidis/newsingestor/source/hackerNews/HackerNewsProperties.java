package io.github.iliasIosifidis.newsingestor.source.hackernews;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "news.hackernews")
public record HackerNewsProperties(
        String baseUrl,
        @DefaultValue("20") int maxStories) {}
