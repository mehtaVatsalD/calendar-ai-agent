package com.commoncoder.calendar.ai.agent.tools;

import com.commoncoder.calendar.ai.agent.tools.constants.ToolDescriptions;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import java.io.IOException;
import javax.annotation.Nullable;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public interface CalendarListTools {

  @Tool(
      name = ToolDescriptions.ListUserCalendarsTool.TOOL_NAME,
      description = ToolDescriptions.ListUserCalendarsTool.TOOL_DESCRIPTION)
  CalendarList listCalendarList(
      @ToolParam(
              description =
                  ToolDescriptions.ListUserCalendarsTool.ToolParamDescriptions.MAX_RESULTS,
              required = false)
          @Nullable
          Integer maxResults,
      @ToolParam(
              description =
                  ToolDescriptions.ListUserCalendarsTool.ToolParamDescriptions.MIN_ACCESS_ROLE,
              required = false)
          @Nullable
          String minAccessRole,
      @ToolParam(
              description = ToolDescriptions.ListUserCalendarsTool.ToolParamDescriptions.PAGE_TOKEN,
              required = false)
          @Nullable
          String pageToken,
      @ToolParam(
              description =
                  ToolDescriptions.ListUserCalendarsTool.ToolParamDescriptions.SHOW_DELETED,
              required = false)
          @Nullable
          Boolean showDeleted,
      @ToolParam(
              description =
                  ToolDescriptions.ListUserCalendarsTool.ToolParamDescriptions.SHOW_HIDDEN,
              required = false)
          @Nullable
          Boolean showHidden,
      @ToolParam(
              description = ToolDescriptions.ListUserCalendarsTool.ToolParamDescriptions.SYNC_TOKEN,
              required = false)
          @Nullable
          String syncToken)
      throws IOException;

  @Tool(
      name = ToolDescriptions.GetUserCalendarListEntryTool.TOOL_NAME,
      description = ToolDescriptions.GetUserCalendarListEntryTool.TOOL_DESCRIPTION)
  CalendarListEntry getCalendarListEntry(
      @ToolParam(
              description =
                  ToolDescriptions.GetUserCalendarListEntryTool.ToolParamDescriptions.CALENDAR_ID)
          String calendarId)
      throws IOException;
}
