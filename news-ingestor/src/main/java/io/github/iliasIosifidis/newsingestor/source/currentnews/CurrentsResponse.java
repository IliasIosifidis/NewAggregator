package io.github.iliasIosifidis.newsingestor.source.currentnews;

import java.util.List;

public record CurrentsResponse(String status, List<Item> news) {
  record Item(
          String id,
          String title,
          String description,
          String url,
          String language,
          String published) {}
}
