package com.commoncoder.calendar.ai.agent.tools;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
  @Tool(
      name = "get_current_datetime",
      description =
          "Provides current date and time at a given timezone.")
  public String getCurrentTime(
      @ToolParam(description = "Timezone for which current time is to be queried. It should be valid IANA timezone. If users have not explicitly provided timezone in their query, You must first call 'get_user_calendar_list_entry' tool to retrieve the user's local timezone before calling this tool to ensure date accuracy for 'today' or 'yesterday' queries.")
          String timeZone) {
    ZoneId zoneId = ZoneId.of(timeZone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    System.out.println(now.format(formatter));
    return now.format(formatter);
  }

  @Tool(
      name = "get_todays_date",
      description =
          "Provides today's date in a given timezone.")
  public String getCurrentDate(
      @ToolParam(description = "Timezone for which current or today's date is to be queried. It should be valid IANA timezone. If users have not explicitly provided timezone in their query, You must first call 'get_user_calendar_list_entry' tool to retrieve the user's local timezone before calling this tool to ensure date accuracy for 'today' or 'yesterday' queries.")
          String timeZone) {
    ZoneId zoneId = ZoneId.of(timeZone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println(now.format(formatter));
    return now.format(formatter);
  }
}
