package io.github.iliasIosifidis.newsingestor.source;

import io.github.iliasIosifidis.newsingestor.article.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngestionScheduler {

  private static final Logger log = LoggerFactory.getLogger(IngestionScheduler.class);
  private final List<NewsSource> sources;

  IngestionScheduler(List<NewsSource> sources) {
    this.sources = sources.stream()
            .filter(s -> s.disabledReason().isEmpty())
            .toList();
  }

  @Scheduled(initialDelayString = "PT5S", // Period of Time: 5 seconds
          fixedDelayString = "${news.ingestion.interval:PT15M}") // Default, when the property is missing
  void pollAll(){
    for (NewsSource source :sources){
      try {
        List<Article> articles = source.fetchLatest();
        log.info("{}: fetched {} articles", source.name(), articles.size());
      } catch (Exception e){
        log.warn("{}: fetch failed: {}", source.name(),
                NestedExceptionUtils.getMostSpecificCause(e).toString()
//                e.getMessage()
        );
      }
    }
  }
}
