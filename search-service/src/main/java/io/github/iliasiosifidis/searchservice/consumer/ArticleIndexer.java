package io.github.iliasiosifidis.searchservice.consumer;

import io.github.iliasiosifidis.searchservice.index.ArticleDocument;
import io.github.iliasiosifidis.searchservice.repository.ArticleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ArticleIndexer {

  private static final Logger log = LoggerFactory.getLogger(ArticleIndexer.class);
  private final ArticleSearchRepository repository;

  public ArticleIndexer(ArticleSearchRepository repository) {
    this.repository = repository;
  }

  @RabbitListener(queues = RabbitConfig.QUEUE)
  void onArticle(ArticleMessage message) {
    repository.save(ArticleDocument.of(
            message.source(),
            message.externalId(),
            message.title(),
            message.url(),
            message.summary(),
            message.publishedAt()));
    log.info("Indexed {}:{}", message.source(), message.externalId());
  }
}
