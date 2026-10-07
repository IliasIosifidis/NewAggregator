package io.github.iliasiosifidis.searchservice.consumer;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import tools.jackson.databind.json.JsonMapper;

@Configuration
class RabbitConfig {

  static final String EXCHANGE = "news.articles";
  static final String QUEUE = "search-service.articles";
  static final String DLX = "news.articles.dlx";
  static final String DLQ = "search-service.articles.dlq";

  @Bean
  TopicExchange articlesExchange() {
    return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  Queue articlesQueue() {
    return QueueBuilder.durable(QUEUE)
            .deadLetterExchange(DLX)
            .deadLetterRoutingKey(DLQ)
            .build();
  }

  @Bean
  Binding articlesBinding() {
    return BindingBuilder.bind(articlesQueue()).to(articlesExchange()).with("article.#");
  }

  @Bean
  DirectExchange deadLetterExchange() {
    return new DirectExchange(DLX, true, false);
  }

  @Bean
  Queue deadLetterQueue() {
    return QueueBuilder.durable(DLQ).build();
  }

  @Bean
  Binding deadLetterBinding() {
    return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ);
  }

  @Bean
  MessageConverter messageConverter(JsonMapper jsonMapper) {
    return new JacksonJsonMessageConverter(jsonMapper);
  }
}