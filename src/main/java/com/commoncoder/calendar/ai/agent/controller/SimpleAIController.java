package com.commoncoder.calendar.ai.agent.controller;

import com.commoncoder.calendar.ai.agent.annotations.QueryHandlerRegistry;
import com.commoncoder.calendar.ai.agent.enums.QueryType;
import com.commoncoder.calendar.ai.agent.model.QueryClassification;
import com.commoncoder.calendar.ai.agent.model.UserQueryResponse;
import com.commoncoder.calendar.ai.agent.prompts.SimpleAIFlowPromptLibrary;
import com.commoncoder.calendar.ai.agent.queryhandlers.UserQueryHandler;
import com.commoncoder.calendar.ai.agent.tools.CalendarListTools;
import com.commoncoder.calendar.ai.agent.tools.EventTools;
import com.commoncoder.calendar.ai.agent.tools.TimeTools;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleAIController {

  private final ChatClient.Builder clientBuilder;
  private final Map<QueryType, UserQueryHandler> userQueryHandlers;
  private final CalendarListTools calendarListTools;
  private final EventTools eventsTools;
  private final TimeTools timeTools;

  @Autowired
  public SimpleAIController(
      ChatClient.Builder clientBuilder,
      @QueryHandlerRegistry Map<QueryType, UserQueryHandler> userQueryHandlers,
      CalendarListTools calendarListTools,
      EventTools eventsTools,
      TimeTools timeTools) {
    this.clientBuilder = clientBuilder;
    this.userQueryHandlers = userQueryHandlers;
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
            .system(SimpleAIFlowPromptLibrary.forSingleStepHandling())
            .user(q)
            .tools(timeTools, calendarListTools, eventsTools)
            .call();
    return v.content();
  }

  @GetMapping("/ai/v2/")
  public UserQueryResponse getAIV2Response(@RequestParam String q) {
    ChatClient client = clientBuilder.build();
    QueryClassification queryClassification =
        client
            .prompt()
            .system(SimpleAIFlowPromptLibrary.forQueryClassification())
            .user(q)
            .call()
            .entity(QueryClassification.class);
    if (queryClassification == null
        || !userQueryHandlers.containsKey(queryClassification.queryType())) {
      return new UserQueryResponse("This feature is not supported", queryClassification.toString());
    }
    return userQueryHandlers.get(queryClassification.queryType()).handle(q);
  }
}
