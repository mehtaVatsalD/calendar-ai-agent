package com.commoncoder.calendar.ai.agent.tools;

import com.commoncoder.calendar.ai.agent.tools.constants.ToolDescriptions.ListEventsTool;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.IOException;

@Service
public class EventsService {

  private final Calendar calendar;

  @Autowired
  public EventsService(Calendar calendar) {
    this.calendar = calendar;
  }

  @Tool(name = ListEventsTool.TOOL_NAME, description = ListEventsTool.TOOL_DESCRIPTION)
  public Events listEvents(
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.CALENDAR_ID) String calendarId,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.Q, required = false) @Nullable
          String q,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.TIME_MIN, required = false)
          @Nullable
          String timeMin,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.TIME_MAX, required = false)
          @Nullable
          String timeMax,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.MAX_RESULTS, required = false)
          @Nullable
          Integer maxResults,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.SINGLE_EVENTS, required = false)
          @Nullable
          Boolean singleEvents,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.ORDER_BY, required = false)
          @Nullable
          String orderBy,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.EVENT_TYPES, required = false)
          @Nullable
          String eventTypes,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.SHOW_DELETED, required = false)
          @Nullable
          Boolean showDeleted,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.PAGE_TOKEN, required = false)
          @Nullable
          String pageToken,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.TIME_ZONE, required = false)
          @Nullable
          String timeZone,
      @ToolParam(description = ListEventsTool.ToolParamDescriptions.UPDATED_MIN, required = false)
          @Nullable
          String updatedMin)
      throws IOException {
    Calendar.Events.List list = calendar.events().list(calendarId);
    if (q != null && !q.isEmpty()) {
      list.setQ(q);
    }
    if (timeMin != null && !timeMin.isEmpty()) {
      list.setTimeMin(DateTime.parseRfc3339(timeMin));
    }
    if (timeMax != null && !timeMax.isEmpty()) {
      list.setTimeMax(DateTime.parseRfc3339(timeMax));
    }
    if (maxResults != null && maxResults > 0) {
      list.setMaxResults(maxResults);
    }
    if (singleEvents != null && singleEvents) {
      list.setSingleEvents(true);
    }
    if (orderBy != null && !orderBy.isEmpty()) {
      list.setOrderBy(orderBy);
    }
    if (eventTypes != null && !eventTypes.isEmpty()) {
      list.set("eventTypes", eventTypes);
    }
    if (showDeleted != null && showDeleted) {
      list.setShowDeleted(true);
    }
    if (pageToken != null && !pageToken.isEmpty()) {
      list.setPageToken(pageToken);
    }
    if (timeZone != null && !timeZone.isEmpty()) {
      list.setTimeZone(timeZone);
    }
    if (updatedMin != null && !updatedMin.isEmpty()) {
      list.setUpdatedMin(DateTime.parseRfc3339(updatedMin));
    }
    return list.execute();
  }
}
