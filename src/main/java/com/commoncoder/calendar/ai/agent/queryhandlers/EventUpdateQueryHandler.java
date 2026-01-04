package com.commoncoder.calendar.ai.agent.queryhandlers;

import com.commoncoder.calendar.ai.agent.model.FindEventForUpdateContext;
import com.commoncoder.calendar.ai.agent.model.UserQueryResponse;
import com.commoncoder.calendar.ai.agent.prompts.EventUpdatePrompts;
import com.commoncoder.calendar.ai.agent.tools.CalendarListTools;
import com.commoncoder.calendar.ai.agent.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

public class EventUpdateQueryHandler implements UserQueryHandler {

  private final ChatClient.Builder clientBuilder;
  private final TimeTools timeTools;
  private final CalendarListTools calendarListTools;

  @Autowired
  public EventUpdateQueryHandler(
      ChatClient.Builder clientBuilder, TimeTools timeTools, CalendarListTools calendarListTools) {
    this.clientBuilder = clientBuilder;
    this.timeTools = timeTools;
    this.calendarListTools = calendarListTools;
  }

  @Override
  public UserQueryResponse handle(String userQuery) {
    FindEventForUpdateContext findEventForUpdateContext =
        clientBuilder
            .build()
            .prompt()
            .system(EventUpdatePrompts.forDerivingContextToFindEventForUpdate())
            .user(userQuery)
            .tools(timeTools, calendarListTools)
            .call()
            .entity(FindEventForUpdateContext.class);
    return new UserQueryResponse("", findEventForUpdateContext.toString(), "");
  }
}
