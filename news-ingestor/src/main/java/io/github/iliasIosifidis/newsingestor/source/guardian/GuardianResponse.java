package io.github.iliasIosifidis.newsingestor.source.guardian;

import java.util.List;

public record GuardianResponse(Body response) {

  record Body(List<Result> results){}

  record Result(
          String id,
          String webTitle,
          String webUrl,
          String webPublicationDate,
          Fields fields){}

  record Fields(String trailText){}
}
