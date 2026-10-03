package io.github.iliasIosifidis.newsingestor.source.guardian;

import io.github.iliasIosifidis.newsingestor.article.Article;
import io.github.iliasIosifidis.newsingestor.source.NewsSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GuardianSource implements NewsSource {

  private final GuardianProperties props;
  private final RestClient restClient; // null when disabled
  private final String disabledReason; // null when enabled

  GuardianSource(RestClient.Builder builder, GuardianProperties props) {
    this.props = props;
    String reason = validate(props);
    RestClient client = null;
    if (reason == null){
      try{
        client = builder.baseUrl(props.baseUrl()).build();
      } catch (RuntimeException e){
        reason = "invalid base URL: " +e.getMessage();
      }
    }
    this.restClient = client;
    this.disabledReason = reason;
  }

  private static String validate(GuardianProperties props) {
    if (props.apiKey() == null || props.apiKey().isBlank()) {return "missing API key";}
    if (props.baseUrl() == null || props.baseUrl().isBlank()) {return "missing base URL";}
    if (props.pageSize() <= 0) {return "page size must be positive";}
    return null;
  }

  @Override
  public String name() {
    return "guardian";
  }

  @Override
  public Optional<String> disabledReason(){
    return Optional.ofNullable(disabledReason);
  }

  @Override
  public List<Article> fetchLatest() {
    // Guard Clause pattern
    if (disabledReason != null){
      throw new IllegalStateException("guardian is disabled: " + disabledReason);
    }
    GuardianResponse response = restClient.get()
            .uri(uri -> uri.path("/search")
                    .queryParam("order-by", "newest")
                    .queryParam("page-size", props.pageSize())
                    .queryParam("show-fields", "trailText")
                    .queryParam("api-key", props.apiKey())
                    .build())
            .retrieve()
            .body(GuardianResponse.class);

    if (response == null || response.response() == null || response.response().results() == null){
      throw new IllegalStateException("unexpected response shape");
    }

    List<Article> articles = new ArrayList<>();
    for (GuardianResponse.Result r : response.response().results()){
      try {
        articles.add(toArticle(r));
      } catch (RuntimeException e){
        log.warn("guardian skipped article {}: {} ", r.id(), e.getMessage());
      }
    }
    return articles;
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
