package io.github.iliasiosifidis.searchservice.activity;

import co.elastic.clients.elasticsearch._types.SortOrder;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {
  private static final List<String> SERVICES = List.of("news-ingestor", "article-store", "search-service");
  private static final int PER_SERVICE = 5;

  private final ElasticsearchOperations operations;

  public ActivityService(ElasticsearchOperations operations) {
    this.operations = operations;
  }

  public Map<String, List<ActivityLog>> latest(){
    Map<String, List<ActivityLog>> result = new LinkedHashMap<>();
    for (String service : SERVICES){
      result.put(service, latestFor(service));
    }
    return result;
  }

  private List<ActivityLog> latestFor(String service){
    NativeQuery query = NativeQuery.builder()
            .withQuery(q -> q.bool( b -> b
                    .filter(f -> f.term(t -> t.field("log.logger").value("activity")))
                    .filter(f -> f.term(t -> t.field("service.name").value(service)))))
            .withSort(s -> s.field(fs -> fs.field("@timestamp").order(SortOrder.Desc)))
            .withMaxResults(PER_SERVICE)
            .build();
    return operations.search(query, ActivityLog.class).getSearchHits().stream()
            .map(SearchHit::getContent)
            .toList();
  }
}
