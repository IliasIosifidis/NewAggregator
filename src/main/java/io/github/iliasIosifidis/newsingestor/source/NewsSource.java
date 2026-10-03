package io.github.iliasIosifidis.newsingestor.source;

import io.github.iliasIosifidis.newsingestor.article.Article;

import java.util.List;

public interface NewsSource {
  String name();
  List<Article> fetchLatest();
}
