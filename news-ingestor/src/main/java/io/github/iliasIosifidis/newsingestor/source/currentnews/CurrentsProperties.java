package io.github.iliasIosifidis.newsingestor.source.currentnews;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "news.currents")
public record CurrentsProperties(
   String baseUrl,
   String apiKey) {}
