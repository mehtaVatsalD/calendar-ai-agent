package com.commoncoder.calendar.ai.agent.tools.model;

import javax.annotation.Nullable;
import org.springframework.ai.tool.annotation.ToolParam;

public record Attendee(
    @ToolParam(
            description =
                "The attendee's email address, if available. This field must be present when adding an attendee. It must be a valid email address as per RFC5322. Required when adding an attendee.")
        String email,
    @ToolParam(
            description = "Whether this is an optional attendee. Optional. The default is False.",
            required = false)
        @Nullable
        Boolean optional) {}
