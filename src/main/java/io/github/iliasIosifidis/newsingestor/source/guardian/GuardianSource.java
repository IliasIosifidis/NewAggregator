package io.github.iliasIosifidis.newsingestor.source.guardian;

import io.github.iliasIosifidis.newsingestor.article.Article;
import io.github.iliasIosifidis.newsingestor.source.NewsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GuardianSource implements NewsSource {

  private final RestClient restClient;
  private final GuardianProperties props;

  GuardianSource(RestClient.Builder builder, GuardianProperties props) {
    this.props = props;
    this.restClient = builder.baseUrl(props.baseUrl()).build();
  }

  @Override
  public String name() {
    return "guardian";
  }

  @Override
  public List<Article> fetchLatest() {
    GuardianResponse response = restClient.get()
            .uri(uri -> uri.path("/search")
                    .queryParam("order-by", "newest")
                    .queryParam("page-size", props.pageSize())
                    .queryParam("show-fields", "trailText")
                    .queryParam("api-key", props.apiKey())
                    .build())
            .retrieve()
            .body(GuardianResponse.class);

    return response.response().results().stream()
            .map(this::toArticle)
            .toList();
  }

  private Article toArticle(GuardianResponse.Result r){
    String summary = r.fields() != null ? r.fields().trailText() : null;
    return new Article(
            name(),
            r.id(),
            r.webTitle(),
            r.webUrl(),
            summary,
            r.webPublicationDate());
  }
}
