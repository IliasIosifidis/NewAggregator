package io.github.iliasiosifidis.searchservice.backfill;

import java.time.Instant;
import java.util.List;

public record ArticlePage(List<Item> content, PageInfo page) {
  record Item(String source, String externalId, String title, String url, String summary, Instant publishedAt){}
  record PageInfo(long totalElements, int totalPages){}
}
