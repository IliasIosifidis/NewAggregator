package io.github.iliasIosifidis.newsingestor.publishing;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitConfig {
  static final String EXCHANGE = "news.articles";

  @Bean
  TopicExchange articleExchange(){
    return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  MessageConverter messageConverter(JsonMapper jsonMapper){
    return new JacksonJsonMessageConverter(jsonMapper);
  }
}
