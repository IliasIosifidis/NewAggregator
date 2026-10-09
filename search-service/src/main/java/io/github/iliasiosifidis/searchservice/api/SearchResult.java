package io.github.iliasiosifidis.searchservice.api;

import io.github.iliasiosifidis.searchservice.index.ArticleDocument;

import java.time.Instant;

public record SearchResult(
        String source,
        String externalId,
        String title,
        String url,
        String summary,
        Instant publishedAt) {

  static SearchResult from(ArticleDocument document){
    return new SearchResult(
            document.source(),
            document.externalId(),
            document.title(),
            document.url(),
            document.summary(),
            document.publishedAt());
  }
}
