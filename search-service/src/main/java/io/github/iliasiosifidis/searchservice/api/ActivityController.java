package io.github.iliasiosifidis.searchservice.api;

import io.github.iliasiosifidis.searchservice.activity.ActivityLog;
import io.github.iliasiosifidis.searchservice.activity.ActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityService activityService;

  public ActivityController(ActivityService activityService) {
    this.activityService = activityService;
  }

  @GetMapping()
  Map<String, List<ActivityEntry>> activity(){
    Map<String, List<ActivityEntry>> result = new LinkedHashMap<>();
    activityService.latest().forEach((service, logs) ->
            result.put(service, logs.stream().map(ActivityEntry::from).toList()));
    return result;
  }

  record ActivityEntry(Instant timestamp, String message){
    static ActivityEntry from(ActivityLog log){
      return new ActivityEntry(log.timestamp(), log.message());
    }
  }
}
