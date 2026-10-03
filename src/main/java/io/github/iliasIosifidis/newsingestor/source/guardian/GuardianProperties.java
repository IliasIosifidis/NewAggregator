package io.github.iliasIosifidis.newsingestor.source.guardian;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "news.guardian")
public record GuardianProperties(
        String baseUrl,
        String apiKey,
        int pageSize) {}
