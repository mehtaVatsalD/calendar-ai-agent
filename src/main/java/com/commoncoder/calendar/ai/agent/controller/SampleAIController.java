package com.commoncoder.calendar.ai.agent.controller;

import com.commoncoder.calendar.ai.agent.tools.CalendarListService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

  @Autowired
  public SampleAIController(
      ChatClient.Builder clientBuilder, CalendarListService calendarListService) {
    this.clientBuilder = clientBuilder;
    this.calendarListService = calendarListService;
  }

  @GetMapping("/ai")
  public String getAIResponse() {
    ChatClient client = clientBuilder.build();
    return client
        .prompt(Prompt.builder().content("Provide the name and summary of all the calendars I have").build())
        .tools(calendarListService)
        .call()
        .content();
  }
}
