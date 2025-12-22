package com.commoncoder.calendar.ai.agent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @Autowired
  public SampleAIController(ChatClient.Builder clientBuilder) {
    this.clientBuilder = clientBuilder;
  }

  @GetMapping("/ai")
  public String getAIResponse(@RequestParam String q) {
    ChatClient client = clientBuilder.build();
    return client
        .prompt(
            Prompt.builder()
                .content(
                    "User might provide name of some location like city, state area etc. Get to know the timezone of the same before answering. What is current date and time in  "
                        + q
                        + "?")
                .build())
        .tools(new TimeService())
        .call()
        .content();
  }
}
