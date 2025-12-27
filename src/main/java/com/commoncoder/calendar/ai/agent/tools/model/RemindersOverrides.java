package com.commoncoder.calendar.ai.agent.tools.model;

import org.springframework.ai.tool.annotation.ToolParam;

public record RemindersOverrides(
        @ToolParam(description = """
                The method used by this reminder. Possible values are:
                "email" - Reminders are sent via email.
                "popup" - Reminders are sent via a UI popup.
                Required when adding a reminder.""")
        String method,

        @ToolParam(description = "Number of minutes before the start of the event when the reminder should trigger. Valid values are between 0 and 40320 (4 weeks in minutes).\n" +
                "Required when adding a reminder.")
        Integer minutes
) {
}
