package io.github.iliasIosifidis.newsingestor.publishing;

import io.github.iliasIosifidis.newsingestor.article.Article;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class ArticlePublisher {
  private final RabbitTemplate rabbitTemplate;

  public ArticlePublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publish(Article article){
    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE,"article." + article.source(), article);
  }
}
