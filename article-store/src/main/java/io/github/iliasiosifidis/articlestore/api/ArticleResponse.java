package io.github.iliasiosifidis.articlestore.api;

import io.github.iliasiosifidis.articlestore.article.Article;

import java.time.Instant;

public record ArticleResponse(
        Long id,
        String source,
        String title,
        String url,
        String summary,
        Instant publishedAt) {

  static ArticleResponse from(Article article){
    return new ArticleResponse(
            article.getId(),
            article.getSource(),
            article.getTitle(),
            article.getUrl(),
            article.getSummary(),
            article.getPublishedAt());
  }
}
