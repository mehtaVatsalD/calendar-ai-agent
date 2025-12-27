package com.commoncoder.calendar.ai.agent.controller;

import com.commoncoder.calendar.ai.agent.tools.CalendarListService;
import com.commoncoder.calendar.ai.agent.tools.EventsService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class TimeService {

  @Tool(description = "Helps in getting current time given a timezone.")
  public String getCurrentTime(
      @ToolParam(description = "Timezone for which current time is to be queried.")
          String timezone) {
    ZoneId zoneId = ZoneId.of(timezone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return now.format(formatter);
  }
}

@RestController
public class SampleAIController {

  private final ChatClient.Builder clientBuilder;
  private final CalendarListService calendarListService;
  private final EventsService eventsService;

  @Autowired
  public SampleAIController(
      ChatClient.Builder clientBuilder,
      CalendarListService calendarListService,
      EventsService eventsService) {
    this.clientBuilder = clientBuilder;
    this.calendarListService = calendarListService;
    this.eventsService = eventsService;
  }

  @GetMapping("/ai")
  public String getAIResponse(@RequestParam String q) {
    ChatClient client = clientBuilder.build();
    return client
        .prompt(Prompt.builder().content(q).build())
        .tools(calendarListService, eventsService)
        .call()
        .content();
  }
}
