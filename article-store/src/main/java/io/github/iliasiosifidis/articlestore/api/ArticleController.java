package io.github.iliasiosifidis.articlestore.api;

import io.github.iliasiosifidis.articlestore.article.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
class ArticleController {

  private final ArticleRepository repository;

  ArticleController(ArticleRepository repository) {
    this.repository = repository;
  }


  @GetMapping
  Page<ArticleResponse> list(
          @RequestParam(required = false) String source,
          @PageableDefault(
                  size = 20,
                  sort = "publishedAt",
                  direction = Sort.Direction.DESC)
          Pageable pageable) {
    if (source == null || source.isBlank()){
      return repository.findAll(pageable).map(ArticleResponse::from);
    }
    return repository.findBySource(source, pageable).map(ArticleResponse::from);
  }
}
