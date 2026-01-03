package com.commoncoder.calendar.ai.agent.controller;

import com.commoncoder.calendar.ai.agent.model.QueryClassification;
import com.commoncoder.calendar.ai.agent.prompts.SystemPromptLibrary;
import com.commoncoder.calendar.ai.agent.tools.CalendarListTools;
import com.commoncoder.calendar.ai.agent.tools.EventTools;
import com.commoncoder.calendar.ai.agent.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleAIController {

  private final ChatClient.Builder clientBuilder;
  private final CalendarListTools calendarListTools;
  private final EventTools eventsTools;
  private final TimeTools timeTools;

  @Autowired
  public SimpleAIController(
      ChatClient.Builder clientBuilder,
      CalendarListTools calendarListTools,
      EventTools eventsTools,
      TimeTools timeTools) {
    this.clientBuilder = clientBuilder;
    this.calendarListTools = calendarListTools;
    this.eventsTools = eventsTools;
    this.timeTools = timeTools;
  }

  @GetMapping("/ai/v1")
  public String getAIV1Response(@RequestParam String q) {
    ChatClient client = clientBuilder.build();
    var v =
        client
            .prompt()
            .system(SystemPromptLibrary.forSingleStepHandling())
            .user(q)
            .tools(timeTools, calendarListTools, eventsTools)
            .call();
    return v.content();
  }

  @GetMapping("/ai/v2/")
  public QueryClassification getAIV2Response(@RequestParam String q) {
    ChatClient client = clientBuilder.build();
    return client
        .prompt()
        .system(SystemPromptLibrary.forQueryClassification())
        .user(q)
        .call()
        .entity(QueryClassification.class);
  }
}
