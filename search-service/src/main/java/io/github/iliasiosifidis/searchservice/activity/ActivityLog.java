package io.github.iliasiosifidis.searchservice.activity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "filebeat-*", createIndex = false)
public record ActivityLog(
        @Id String id,
        @Field(name = "@timestamp", type = FieldType.Date) Instant timestamp,
        Service service,
        String message
        ) {
  public record Service(String name){
  }
}

