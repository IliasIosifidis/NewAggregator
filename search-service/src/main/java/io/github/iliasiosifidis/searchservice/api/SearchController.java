package io.github.iliasiosifidis.searchservice.api;

import io.github.iliasiosifidis.searchservice.search.ArticleSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

  private final ArticleSearchService searchService;

  public SearchController(ArticleSearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping
  List<SearchResult> search(
          @RequestParam String q,
          @RequestParam(required = false) String source,
          @RequestParam(defaultValue = "10") int size){
    return searchService.search(q, source, size).stream()
            .map(SearchResult::from)
            .toList();
  }
}
