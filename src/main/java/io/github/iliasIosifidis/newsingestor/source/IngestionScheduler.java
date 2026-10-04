package io.github.iliasIosifidis.newsingestor.source;

import io.github.iliasIosifidis.newsingestor.article.Article;
import io.github.iliasIosifidis.newsingestor.dedup.SeenArticles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngestionScheduler {

  private static final Logger log = LoggerFactory.getLogger(IngestionScheduler.class);
  private final List<NewsSource> sources;
  private final SeenArticles seenArticles;

  IngestionScheduler(List<NewsSource> sources, SeenArticles seenArticles) {
    this.sources = sources.stream()
            .filter(s -> s.disabledReason().isEmpty())
            .toList();
    this.seenArticles = seenArticles;
  }

  @Scheduled(initialDelayString = "PT5S", // Period of Time: 5 seconds
          fixedDelayString = "${news.ingestion.interval:PT15M}") // Default, when the property is missing
  void pollAll(){
    for (NewsSource source :sources){
      try {
        List<Article> articles = source.fetchLatest(); // fetch
        List<Article> fresh = new ArrayList<>();       // keep only new ones
        for (Article article : articles){
          if (seenArticles.markIfNew(article)){
            fresh.add(article);
          }
        }
        log.info("{}: fetched {} articles, {} new", source.name(), articles.size(), fresh.size());

      } catch (HttpClientErrorException e){
        log.error("{}: request rejected with {}, check this source's configuration",
                source.name(), e.getStatusCode());
      } catch (HttpServerErrorException e){
        log.warn("{}: source returned {}, will retry next cycle",
                source.name(), e.getStatusCode());
      } catch (ResourceAccessException e){
        log.warn("{}: unreachable: {}", source.name(),
                NestedExceptionUtils.getMostSpecificCause(e).toString());
      } catch (Exception e){
        log.error("{}: unexpected failure: {}", source.name(),
                NestedExceptionUtils.getMostSpecificCause(e).toString());
      }
    }
  }
}
