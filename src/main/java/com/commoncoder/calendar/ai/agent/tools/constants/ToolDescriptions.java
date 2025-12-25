package com.commoncoder.calendar.ai.agent.tools.constants;

public class ToolDescriptions {

  public static class ListUserCalendarsTool {
    public static final String TOOL_NAME = "list_user_calendars";
    public static final String TOOL_DESCRIPTION =
        "Returns the calendars on the user's calendar list. The CalendarList is a collection of all calendar entries that a user has added to their list (shown in the left panel of the web UI). You can use it to add and remove existing calendars to/from the users’ list. You also use it to retrieve and set the values of user-specific calendar properties, such as default reminders. Another example is foreground color, since different users can have different colors set for the same calendar.";

    public static class ToolParamDescriptions {
      public static final String MAX_RESULTS =
          "Maximum number of entries returned on one result page. By default the value is 100 entries. The page size can never be larger than 250 entries. Optional.";

      public static final String MIN_ACCESS_ROLE =
          "The minimum access role for the user in the returned entries. The default is no restriction. Optional. Acceptable values are: 'freeBusyReader', 'reader', 'writer', 'owner'.";

      public static final String PAGE_TOKEN =
          "Token specifying which result page to return from a previous list request. This is used for pagination. Optional.";

      public static final String SHOW_DELETED =
          "Whether to include deleted calendar entries in the result. The default is False. Optional.";

      public static final String SHOW_HIDDEN =
          "Whether to include hidden calendar entries in the result. The default is False. Optional.";

      public static final String SYNC_TOKEN =
          "Token obtained from the nextSyncToken field of a previous list request. It is used to get only the entries that have changed since then. Optional.";
    }
  }

  public static class GetUserCalendarListEntryTool {
    public static final String TOOL_NAME = "get_user_calendar_list_entry";
    public static final String TOOL_DESCRIPTION =
        "Returns a entry of the calendar from the user's calendar list i.e. single instance of calendar that is received from CalendarList.list";

    public static class ToolParamDescriptions {
      public static final String CALENDAR_ID =
          "Calendar identifier. To retrieve calendar IDs call the calendarList.list method. If you want to access the primary calendar of the currently logged in user, use the 'primary' keyword.";
    }
  }

  public static class ListEventsTool {
    public static final String TOOL_NAME = "list_events";
    public static final String TOOL_DESCRIPTION =
        "Returns events on the specified calendar. An event is an object associated with a specific date or time range. Events are identified by a unique ID. Besides a start and end date-time, events contain other data such as summary, description, location, status, reminders, attachments, etc.";

    public static class ToolParamDescriptions {
      public static final String CALENDAR_ID =
          "Calendar identifier. To retrieve calendar IDs call the calendarList.list method. If you want to access the primary calendar of the currently logged in user, use the 'primary' keyword. Required.";

      public static final String Q =
          "Free text search terms to find events that match these terms in fields like summary, description, location, attendee names/emails, etc. Optional.";

      public static final String TIME_MIN =
          "Lower bound (exclusive) for an event's end time to filter by. Must be an RFC3339 timestamp with mandatory time zone offset (e.g., 2011-06-03T10:00:00Z). Optional.";

      public static final String TIME_MAX =
          "Upper bound (exclusive) for an event's start time to filter by. Must be an RFC3339 timestamp with mandatory time zone offset (e.g., 2011-06-03T10:00:00Z). Optional.";

      public static final String MAX_RESULTS =
          "Maximum number of events returned on one result page. By default the value is 250 events. The page size can never be larger than 2500 events. Optional.";

      public static final String SINGLE_EVENTS =
          "Whether to expand recurring events into instances and only return single one-off events and instances of recurring events. The default is False. Optional.";

      public static final String ORDER_BY =
          "The order of the events returned in the result. Acceptable values are 'startTime' (only if singleEvents is True) or 'updated'. Optional.";

      public static final String EVENT_TYPES =
          "Event types to return. Acceptable values are: 'birthday', 'default', 'focusTime', 'fromGmail', 'outOfOffice', 'workingLocation'. Optional.";

      public static final String SHOW_DELETED =
          "Whether to include deleted events (status equals 'cancelled') in the result. The default is False. Optional.";

      public static final String PAGE_TOKEN =
          "Token specifying which result page to return from a previous list request. Optional.";

      public static final String TIME_ZONE =
          "Time zone used in the response. The default is the time zone of the calendar. Optional.";

      public static final String UPDATED_MIN =
          "Lower bound for an event's last modification time (RFC3339 timestamp) to filter by. Optional.";
    }
  }
}
