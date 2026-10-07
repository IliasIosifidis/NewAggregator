package io.github.iliasiosifidis.searchservice.repository;

import io.github.iliasiosifidis.searchservice.index.ArticleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleSearchRepository extends ElasticsearchRepository<ArticleDocument, String > {
}
