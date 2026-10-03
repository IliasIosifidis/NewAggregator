package io.github.iliasIosifidis.newsingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NewsIngestorApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsIngestorApplication.class, args);
  }

}
