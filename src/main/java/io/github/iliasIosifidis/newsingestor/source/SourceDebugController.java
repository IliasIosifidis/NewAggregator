package io.github.iliasIosifidis.newsingestor.source;

import io.github.iliasIosifidis.newsingestor.article.Article;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debug/sources")
public class SourceDebugController {
  private final List<NewsSource> sources;

  public SourceDebugController(List<NewsSource> sources) {
    this.sources = sources;
  }

  @GetMapping("/{name}")
  List<Article> fetch(@PathVariable String name){
    return sources.stream()
            .filter(s ->s.name().equals(name))
            .findFirst()
            .orElseThrow()
            .fetchLatest();
  }
}
