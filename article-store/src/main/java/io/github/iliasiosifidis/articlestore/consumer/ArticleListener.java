package io.github.iliasiosifidis.articlestore.consumer;

import io.github.iliasiosifidis.articlestore.ArticleMessage;
import io.github.iliasiosifidis.articlestore.article.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ArticleListener {

  private static final Logger log = LoggerFactory.getLogger(ArticleListener.class);
  private static final Logger activity = LoggerFactory.getLogger("activity");
  private final ArticleRepository repository;

  public ArticleListener(ArticleRepository repository) {
    this.repository = repository;
  }

  @RabbitListener(queues = RabbitConfig.QUEUE)
  void onArticle(ArticleMessage message) {
    int inserted = repository.insertIfAbsent(message.source(), message.externalId(), message.title(), message.url(), message.summary(), message.publishedAt());
    if (inserted == 1){
      log.info("Stored {}:{}", message.source(), message.externalId());
      activity.info("Stored \"{}\" from {}", message.title(), message.source());
    } else {
      log.info("Duplicate ignored {}:{}", message.source(), message.externalId());
    }
  }
}
