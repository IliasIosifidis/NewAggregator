package io.github.iliasIosifidis.newsingestor.source.currentnews;

import io.github.iliasIosifidis.newsingestor.article.Article;
import io.github.iliasIosifidis.newsingestor.source.NewsSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class CurrentsSource implements NewsSource {

  private final CurrentsProperties properties;
  private final RestClient restClient;
  private final String disabledReason;

  private static final DateTimeFormatter PUBLISHED =
          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

  public CurrentsSource(CurrentsProperties properties, RestClient.Builder builder) {
    this.properties = properties;
    String reason = validate(properties);
    RestClient restClient = null;
    if (reason == null) {
      try {
        restClient = builder
                .baseUrl(properties.baseUrl())
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(properties.apiKey()))
                .build();
      } catch (RuntimeException e) {
        reason = "invalid base URL: " + e.getMessage();
      }
    }
    this.restClient = restClient;
    this.disabledReason = reason;
  }

  @Override
  public Optional<String> disabledReason() {
    return Optional.ofNullable(disabledReason);
  }

  private String validate(CurrentsProperties properties) {
    if (properties.baseUrl() == null || properties.baseUrl().isBlank()) {
      return "missing baseUrl";
    }
    if (properties.apiKey() == null || properties.apiKey().isBlank()) {
      return "missing API";
    }
    return null;
  }

  @Override
  public String name() {
    return "currents";
  }

  @Override
  public List<Article> fetchLatest() {
    if (disabledReason != null) {
      throw new IllegalStateException("Currents is disabled" + disabledReason);
    }
    CurrentsResponse response = restClient.get()
            .uri(uri -> uri.path("/latest-news")
                    .queryParam("language", "en")
                    .build())
            .retrieve()
            .body(CurrentsResponse.class);
    if (response == null) {
      throw new IllegalStateException("unexpected response shape");
    }
    if (!"ok".equalsIgnoreCase(response.status())){
      throw new IllegalStateException("response status: " + response.status());
    }
    List<Article> articles = new ArrayList<>();

    for (CurrentsResponse.Item item : response.news()) {
      try {
        articles.add(toArticle(item));
      } catch (RuntimeException e) {
        log.warn("Currents skipped item {}: {}", item, e.getMessage());
      }
    }
    return articles;
  }

  private Article toArticle(CurrentsResponse.Item item) {
    return new Article(
            name(),
            item.id(),
            item.title(),
            item.url(),
            item.description(),
            OffsetDateTime.parse(item.published(), PUBLISHED).toInstant());
  }
}























