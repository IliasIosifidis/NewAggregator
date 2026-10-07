package io.github.iliasiosifidis.searchservice.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "articles")
public record ArticleDocument(
        @Id
        String id,
        @Field(type = FieldType.Keyword) String source,
        @Field(type = FieldType.Keyword) String externalId,
        @Field(type = FieldType.Search_As_You_Type) String title,
        @Field(type = FieldType.Text) String summary,
        @Field(type = FieldType.Keyword, index = false) String url,
        @Field(type = FieldType.Date) Instant publishedAt) {

  public static ArticleDocument of(String source, String externalId, String title,
                            String url, String summary, Instant publishedAt) {
    if (source == null || source.isBlank() || externalId == null || externalId.isBlank()){
      throw new IllegalArgumentException("Source and externalId are required for the document key");
    }
    return new ArticleDocument(source + ":" + externalId, source, externalId, title, stripHtml(summary), url, publishedAt);
  }

  private static String stripHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("<[^>]*>", "").strip();
  }
}
