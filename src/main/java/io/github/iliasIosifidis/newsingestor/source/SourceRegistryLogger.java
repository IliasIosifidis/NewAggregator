package io.github.iliasIosifidis.newsingestor.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SourceRegistryLogger implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(SourceRegistryLogger.class);
  private final List<NewsSource> sources;

  public SourceRegistryLogger(List<NewsSource> sources) {
    this.sources = sources;
  }

  @Override
  public void run(ApplicationArguments args){
    log.info("Loaded {} news source(s): {}", sources.size(),
            sources.stream().map(NewsSource::name).toList());
  }
}
