package com.commoncoder.calendar.ai.agent.tools;

import com.commoncoder.calendar.ai.agent.tools.constants.ToolDescriptions.ListEventsTool;
import com.commoncoder.calendar.ai.agent.tools.model.InsertEventRequestBody;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    System.out.println(timeMin);
    System.out.println(timeMax);
    System.out.println(timeZone);
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
    Events events = list.execute();
    System.out.println(events);
    return events;
  }

  @Tool(name = "insert_event", description = "Creates an event on the calendar.")
  Event insertEvent(
      @ToolParam(
              description =
                  "Calendar identifier. To retrieve calendar IDs call the calendarList.list method. If you want to access the primary calendar of the currently logged in user, use the 'primary' keyword.")
          String calendarId,
      @ToolParam(description = "Event request body") InsertEventRequestBody insertEventRequestBody)
      throws IOException {
    Event event = new Event();
    event.setSummary(insertEventRequestBody.summary());
    EventDateTime start = new EventDateTime();
    if (insertEventRequestBody.start().dateTime() != null
        && !insertEventRequestBody.start().dateTime().isEmpty()) {
      start.setDateTime(DateTime.parseRfc3339(insertEventRequestBody.start().dateTime()));
    }
    if (insertEventRequestBody.start().date() != null
        && !insertEventRequestBody.start().date().isEmpty()) {
      start.setDate(DateTime.parseRfc3339(insertEventRequestBody.start().date()));
    }
    if (insertEventRequestBody.start().timeZone() != null
        && !insertEventRequestBody.start().timeZone().isEmpty()) {
      start.setTimeZone(insertEventRequestBody.start().timeZone());
    }
    event.setStart(start);

    EventDateTime end = new EventDateTime();
    if (insertEventRequestBody.end().dateTime() != null
        && !insertEventRequestBody.end().dateTime().isEmpty()) {
      end.setDateTime(DateTime.parseRfc3339(insertEventRequestBody.end().dateTime()));
    }
    if (insertEventRequestBody.end().date() != null
        && !insertEventRequestBody.end().date().isEmpty()) {
      end.setDate(DateTime.parseRfc3339(insertEventRequestBody.end().date()));
    }
    if (insertEventRequestBody.end().timeZone() != null
        && !insertEventRequestBody.end().timeZone().isEmpty()) {
      end.setTimeZone(insertEventRequestBody.end().timeZone());
    }
    event.setEnd(end);

    if (insertEventRequestBody.visibility() != null) {
      event.setVisibility(insertEventRequestBody.visibility());
    }
    if (insertEventRequestBody.description() != null) {
      event.setDescription(insertEventRequestBody.description());
    }
    if (insertEventRequestBody.attendees() != null) {
      List<EventAttendee> eventAttendees =
          insertEventRequestBody.attendees().stream()
              .map(
                  attendees ->
                      new EventAttendee()
                          .setEmail(attendees.email())
                          .setOptional(attendees.optional()))
              .toList();
      event.setAttendees(eventAttendees);
    }
    if (insertEventRequestBody.guestsCanInviteOthers() != null) {
      event.setGuestsCanInviteOthers(insertEventRequestBody.guestsCanInviteOthers());
    }
    if (insertEventRequestBody.guestsCanModify() != null) {
      event.setGuestsCanModify(insertEventRequestBody.guestsCanModify());
    }
    if (insertEventRequestBody.guestsCanSeeOtherGuests() != null) {
      event.setGuestsCanSeeOtherGuests(insertEventRequestBody.guestsCanSeeOtherGuests());
    }

    if (insertEventRequestBody.location() != null) {
      event.setLocation(insertEventRequestBody.location());
    }

    if (insertEventRequestBody.recurrence() != null) {
      event.setRecurrence(insertEventRequestBody.recurrence());
    }
    Event.Reminders reminders = new Event.Reminders();
    if (insertEventRequestBody.remindersOverrides() != null) {
      reminders.setOverrides(
          insertEventRequestBody.remindersOverrides().stream()
              .map(
                  remindersOverrides ->
                      new EventReminder()
                          .setMethod(remindersOverrides.method())
                          .setMinutes(remindersOverrides.minutes()))
              .toList());
    } else {
      reminders.setUseDefault(true);
    }
    event.setReminders(reminders);
    return calendar.events().insert(calendarId, event).execute();
  }
}
