package com.commoncoder.calendar.ai.agent.tools;

import com.commoncoder.calendar.ai.agent.tools.constants.ToolDescriptions;
import com.commoncoder.calendar.ai.agent.tools.model.EventItem;
import com.commoncoder.calendar.ai.agent.tools.request.InsertEventRequest;
import com.commoncoder.calendar.ai.agent.tools.request.UpdateEventRequest;
import com.commoncoder.calendar.ai.agent.tools.response.EventsResponse;
import java.io.IOException;
import javax.annotation.Nullable;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public interface EventTools {
  @Tool(
      name = ToolDescriptions.ListEventsTool.TOOL_NAME,
      description = ToolDescriptions.ListEventsTool.TOOL_DESCRIPTION)
  EventsResponse listEvents(
      @ToolParam(description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.CALENDAR_ID)
          String calendarId,
      @ToolParam(description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.TIME_MIN)
          String timeMin,
      @ToolParam(description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.TIME_MAX)
          String timeMax,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.Q,
              required = false)
          @Nullable
          String q,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.MAX_RESULTS,
              required = false)
          @Nullable
          Integer maxResults,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.SINGLE_EVENTS,
              required = false)
          @Nullable
          Boolean singleEvents,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.ORDER_BY,
              required = false)
          @Nullable
          String orderBy,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.EVENT_TYPES,
              required = false)
          @Nullable
          String eventTypes,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.SHOW_DELETED,
              required = false)
          @Nullable
          Boolean showDeleted,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.PAGE_TOKEN,
              required = false)
          @Nullable
          String pageToken,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.TIME_ZONE,
              required = false)
          @Nullable
          String timeZone,
      @ToolParam(
              description = ToolDescriptions.ListEventsTool.ToolParamDescriptions.UPDATED_MIN,
              required = false)
          @Nullable
          String updatedMin)
      throws IOException;

  @Tool(name = "insert_event", description = "Creates an event on the calendar.")
  EventItem insertEvent(
      @ToolParam(
              description =
                  "Calendar identifier. To retrieve calendar IDs call the calendarList.list method. If you want to access the primary calendar of the currently logged in user, use the 'primary' keyword.")
          String calendarId,
      @ToolParam(description = "Event request body") InsertEventRequest insertEventRequest)
      throws IOException;

  @Tool(
      name = "patch_event",
      description = "Patches and updates an existing and already created event on the calendar.")
  EventItem updateEvent(
      @ToolParam(
              description =
                  "Calendar identifier. To retrieve calendar IDs call the calendarList.list method. If you want to access the primary calendar of the currently logged in user, use the 'primary' keyword.")
          String calendarId,
      @ToolParam(description = "Event identifier. Event that is to be modified") String eventId,
      @ToolParam(description = "Event request body. Set only the fields that are to be updated")
          UpdateEventRequest updateEventRequest)
      throws IOException;
}
