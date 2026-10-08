package io.github.iliasiosifidis.searchservice.search;

import io.github.iliasiosifidis.searchservice.index.ArticleDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSearchService {

  private static final int MAX_RESULTS = 20;
  private final ElasticsearchOperations operations;

  public ArticleSearchService(ElasticsearchOperations operations) {
    this.operations = operations;
  }

  public List<ArticleDocument> search(String text, String source, int size) {
    if (text == null || text.isBlank()) {
      return List.of();
    }

    NativeQuery query = NativeQuery.builder()
            .withQuery(q -> q.bool(b -> {
              b.must(m -> m.multiMatch(mm -> mm
                      .query(text)
                      .type(TextQueryType.BoolPrefix)
                      .fields("title^3", "title._2gram^3", "title._3gram^3", "summary")
                      .fuzziness("AUTO")));
              if (source != null && !source.isBlank()) {
                b.filter(f -> f.term(t -> t.field("source").value(source)));
              }
              return b;
            }))
            .withPageable(PageRequest.of(0, Math.clamp(size, 1, MAX_RESULTS)))
            .build();

    return operations.search(query, ArticleDocument.class)
            .getSearchHits().stream()
            .map(SearchHit::getContent)
            .toList();
  }
  /* ElasticSearch structure we are trying to match
  {
  "query": {
    "bool": {
      "must": {
        "multi_match": {
          "query": "text",
          "type": "bool_prefix",
          "fields": ["title^3", "title._2gram^3", "title._3gram^3", "summary"],
          "fuzziness": "AUTO"
        }
      },
      "filter": {
        "term": { "source": "guardian" }
      }
    }
  }
}
   */
}
