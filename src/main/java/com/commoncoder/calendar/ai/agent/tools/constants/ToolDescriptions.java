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
}
