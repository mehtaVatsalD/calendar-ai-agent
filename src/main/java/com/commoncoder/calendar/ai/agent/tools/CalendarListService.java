package com.commoncoder.calendar.ai.agent.tools;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.commoncoder.calendar.ai.agent.tools.constants.ToolDescriptions.ListUserCalendarsTool;
import com.commoncoder.calendar.ai.agent.tools.constants.ToolDescriptions.GetUserCalendarListEntryTool;

import javax.annotation.Nullable;
import java.io.IOException;

@Service
public class CalendarListService {

  private final Calendar calendar;

  @Autowired
  public CalendarListService(Calendar calendar) {
    this.calendar = calendar;
  }

  @Tool(
      name = ListUserCalendarsTool.TOOL_NAME,
      description = ListUserCalendarsTool.TOOL_DESCRIPTION)
  public CalendarList listCalendarList(
      @ToolParam(
              description = ListUserCalendarsTool.ToolParamDescriptions.MAX_RESULTS,
              required = false)
          @Nullable
          Integer maxResults,
      @ToolParam(
              description = ListUserCalendarsTool.ToolParamDescriptions.MIN_ACCESS_ROLE,
              required = false)
          @Nullable
          String minAccessRole,
      @ToolParam(
              description = ListUserCalendarsTool.ToolParamDescriptions.PAGE_TOKEN,
              required = false)
          @Nullable
          String pageToken,
      @ToolParam(
              description = ListUserCalendarsTool.ToolParamDescriptions.SHOW_DELETED,
              required = false)
          @Nullable
          Boolean showDeleted,
      @ToolParam(
              description = ListUserCalendarsTool.ToolParamDescriptions.SHOW_HIDDEN,
              required = false)
          @Nullable
          Boolean showHidden,
      @ToolParam(
              description = ListUserCalendarsTool.ToolParamDescriptions.SYNC_TOKEN,
              required = false)
          @Nullable
          String syncToken)
      throws IOException {
    Calendar.CalendarList.List list = calendar.calendarList().list();
    if (maxResults != null && maxResults > 0) {
      list.setMaxResults(maxResults);
    }
    if (minAccessRole != null && !minAccessRole.isEmpty()) {
      list.setMinAccessRole(minAccessRole);
    }
    if (pageToken != null && !pageToken.isEmpty()) {
      list.setPageToken(pageToken);
    }
    if (showDeleted != null && showDeleted) {
      list.setShowDeleted(true);
    }
    if (showHidden != null && showHidden) {
      list.setShowHidden(true);
    }
    if (syncToken != null && !syncToken.isEmpty()) {
      list.setSyncToken(syncToken);
    }
    return list.execute();
  }

  @Tool(
      name = GetUserCalendarListEntryTool.TOOL_NAME,
      description = GetUserCalendarListEntryTool.TOOL_DESCRIPTION)
  CalendarListEntry getCalendarListEntry(
      @ToolParam(description = GetUserCalendarListEntryTool.ToolParamDescriptions.CALENDAR_ID)
          String calendarId)
      throws IOException {
    return calendar.calendarList().get(calendarId).execute();
  }
}
