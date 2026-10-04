package io.github.iliasIosifidis.newsingestor.publishing;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitConfig {
  static final String EXCHANGE = "news.articles";

  @Bean
  TopicExchange articleExchange(){
    return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  JacksonJsonMessageConverter messageConverter(JsonMapper jsonMapper){
    return new JacksonJsonMessageConverter(jsonMapper);
  }
}
