package com.commoncoder.calendar.ai.agent.tools.request;

import com.commoncoder.calendar.ai.agent.tools.model.Attendee;
import com.commoncoder.calendar.ai.agent.tools.model.DateTime;
import com.commoncoder.calendar.ai.agent.tools.model.RemindersOverrides;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.ai.tool.annotation.ToolParam;

public record UpdateEventRequest(
    @ToolParam(description = "Title of the event.", required = false) @Nullable String summary,
    @ToolParam(
            description = "Description of the event. Can contain HTML. Optional.",
            required = false)
        @Nullable
        String description,
    @ToolParam(
            description =
                """
                Visibility of the event. Optional. Possible values are:
                "default" - Uses the default visibility for events on the calendar. This is the default value.
                "public" - The event is public and event details are visible to all readers of the calendar.
                "private" - The event is private and only event attendees may view event details.
                "confidential" - The event is private. This value is provided for compatibility reasons.""",
            required = false)
        @Nullable
        String visibility,
    @ToolParam(
            description =
                "The (inclusive) start time of the event. For a recurring event, this is the start time of the first instance.",
            required = false)
        @Nullable
        DateTime start,
    @ToolParam(
            description =
                "The (exclusive) end time of the event. For a recurring event, this is the end time of the first instance.",
            required = false)
        @Nullable
        DateTime end,
    @ToolParam(
            description = "List of the attendees of attendees to be added to the event.",
            required = false)
        @Nullable
        List<Attendee> attendeesToAdd,
    @ToolParam(
            description = "List of the attendees of attendees to be removed from the event.",
            required = false)
        @Nullable
        List<Attendee> attendeesToRemove,
    @ToolParam(
            description =
                "Whether attendees other than the organizer can invite others to the event. Optional. The default is True.",
            required = false)
        @Nullable
        Boolean guestsCanInviteOthers,
    @ToolParam(
            description =
                "Whether attendees other than the organizer can modify the event. Optional. The default is False.",
            required = false)
        @Nullable
        Boolean guestsCanModify,
    @ToolParam(
            description =
                "Whether attendees other than the organizer can see who the event's attendees are. Optional. The default is True.",
            required = false)
        @Nullable
        Boolean guestsCanSeeOtherGuests,
    @ToolParam(
            description = "Geographic location of the event as free-form text. Optional.",
            required = false)
        @Nullable
        String location,
    @ToolParam(
            description =
                "List of RRULE, EXRULE, RDATE and EXDATE lines for a recurring event, as specified in RFC5545. Note that DTSTART and DTEND lines are not allowed in this field; event start and end times are specified in the start and end fields. This field is omitted for single events or instances of recurring events.")
        @Nullable
        List<String> recurrence,
    @ToolParam(
            description =
                "Reminders to be overridden. Set this list to update reminders. Set this field 'remindersOverrides' and not 'reminders.overrides'. The maximum number of override reminders is 5.",
            required = false)
        @Nullable
        List<RemindersOverrides> remindersOverrides) {}
