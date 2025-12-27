package com.commoncoder.calendar.ai.agent.tools.model;

import javax.annotation.Nullable;
import org.springframework.ai.tool.annotation.ToolParam;

public record DateTime(
    @ToolParam(
            description = "The date, in the format 'yyyy-mm-dd', if this is an all-day event.",
            required = false)
        @Nullable
        String date,
    @ToolParam(
            description =
                "The time, as a combined date-time value (formatted according to RFC3339). A time zone offset is required unless a time zone is explicitly specified in timeZone.",
            required = false)
        @Nullable
        String dateTime,
    @ToolParam(
            description =
                "The time zone in which the time is specified. (Formatted as an IANA Time Zone Database name, e.g. \"Europe/Zurich\".) For recurring events this field is required and specifies the time zone in which the recurrence is expanded. For single events this field is optional and indicates a custom time zone for the event start/end.",
            required = false)
        @Nullable
        String timeZone) {}
