package com.commoncoder.calendar.ai.agent.queryhandlers;

import com.commoncoder.calendar.ai.agent.model.FindEventToUpdateContext;
import com.commoncoder.calendar.ai.agent.model.UserQueryResponse;
import com.commoncoder.calendar.ai.agent.prompts.EventUpdatePrompts;
import com.commoncoder.calendar.ai.agent.tools.CalendarListTools;
import com.commoncoder.calendar.ai.agent.tools.EventTools;
import com.commoncoder.calendar.ai.agent.tools.TimeTools;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

public class EventUpdateQueryHandler implements UserQueryHandler {

  private final ChatClient.Builder clientBuilder;
  private final TimeTools timeTools;
  private final CalendarListTools calendarListTools;
  private final EventTools eventsTools;

  @Autowired
  public EventUpdateQueryHandler(
      ChatClient.Builder clientBuilder,
      TimeTools timeTools,
      CalendarListTools calendarListTools,
      EventTools eventsTools) {
    this.clientBuilder = clientBuilder;
    this.timeTools = timeTools;
    this.calendarListTools = calendarListTools;
    this.eventsTools = eventsTools;
  }

  @Override
  public UserQueryResponse handle(String userQuery) {
    ChatClient chatClient = clientBuilder.build();
    FindEventToUpdateContext findEventToUpdateContext =
        chatClient
            .prompt()
            .system(EventUpdatePrompts.forDerivingContextToFindEventForUpdate())
            .user(userQuery)
            .tools(timeTools, calendarListTools)
            .call()
            .entity(FindEventToUpdateContext.class);

    // In future if find + update is too complex, it could be broken down into find + updateContext
    // + update.
    return chatClient
        .prompt()
        .system(
            EventUpdatePrompts.forUpdatingEvent()
                .create(Map.of("FindEventToUpdateContext", findEventToUpdateContext))
                .getContents())
        .user(userQuery)
        .tools(timeTools, calendarListTools, eventsTools)
        .call()
        .entity(UserQueryResponse.class);
  }
}
