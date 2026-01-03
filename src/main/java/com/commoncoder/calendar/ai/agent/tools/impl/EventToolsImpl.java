package com.commoncoder.calendar.ai.agent.tools.impl;

import com.commoncoder.calendar.ai.agent.tools.EventTools;
import com.commoncoder.calendar.ai.agent.tools.model.Attendee;
import com.commoncoder.calendar.ai.agent.tools.model.EventItem;
import com.commoncoder.calendar.ai.agent.tools.request.InsertEventRequest;
import com.commoncoder.calendar.ai.agent.tools.request.UpdateEventRequest;
import com.commoncoder.calendar.ai.agent.tools.response.EventsResponse;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class EventToolsImpl implements EventTools {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventToolsImpl.class);

  private final Calendar calendar;

  @Autowired
  public EventToolsImpl(Calendar calendar) {
    this.calendar = calendar;
  }

  @Override
  public EventsResponse listEvents(
      String calendarId,
      String timeMin,
      String timeMax,
      @Nullable String q,
      @Nullable Integer maxResults,
      @Nullable Boolean singleEvents,
      @Nullable String orderBy,
      @Nullable String eventTypes,
      @Nullable Boolean showDeleted,
      @Nullable String pageToken,
      @Nullable String timeZone,
      @Nullable String updatedMin)
      throws IOException {
    LOGGER.info(
        "listEvents called with timeMin: {}, timeMax: {}, timezone: {}",
        timeMin,
        timeMax,
        timeZone);
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
    return toEventsResponse(list.execute());
  }

  @Override
  public EventItem insertEvent(String calendarId, InsertEventRequest insertEventRequest)
      throws IOException {
    LOGGER.info("insertEvent called");
    Event event = new Event();
    event.setSummary(insertEventRequest.summary());

    event.setStart(toEventDateTime(insertEventRequest.start()));
    event.setEnd(toEventDateTime(insertEventRequest.end()));

    if (insertEventRequest.visibility() != null) {
      event.setVisibility(insertEventRequest.visibility());
    }
    if (insertEventRequest.description() != null) {
      event.setDescription(insertEventRequest.description());
    }
    if (insertEventRequest.attendees() != null) {
      List<EventAttendee> eventAttendees =
          insertEventRequest.attendees().stream()
              .map(
                  attendees ->
                      new EventAttendee()
                          .setEmail(attendees.email())
                          .setOptional(attendees.optional()))
              .toList();
      event.setAttendees(eventAttendees);
    }
    if (insertEventRequest.guestsCanInviteOthers() != null) {
      event.setGuestsCanInviteOthers(insertEventRequest.guestsCanInviteOthers());
    }
    if (insertEventRequest.guestsCanModify() != null) {
      event.setGuestsCanModify(insertEventRequest.guestsCanModify());
    }
    if (insertEventRequest.guestsCanSeeOtherGuests() != null) {
      event.setGuestsCanSeeOtherGuests(insertEventRequest.guestsCanSeeOtherGuests());
    }

    if (insertEventRequest.location() != null) {
      event.setLocation(insertEventRequest.location());
    }

    if (insertEventRequest.recurrence() != null) {
      event.setRecurrence(insertEventRequest.recurrence());
    }
    Event.Reminders reminders = new Event.Reminders();
    if (insertEventRequest.remindersOverrides() != null) {
      reminders.setOverrides(
          insertEventRequest.remindersOverrides().stream()
              .map(
                  remindersOverrides ->
                      new EventReminder()
                          .setMethod(remindersOverrides.method())
                          .setMinutes(remindersOverrides.minutes()))
              .toList());
      reminders.setUseDefault(false);
    } else {
      reminders.setUseDefault(true);
    }
    event.setReminders(reminders);
    return toEventItem(calendar.events().insert(calendarId, event).execute());
  }

  @Override
  public EventItem updateEvent(
      String calendarId, String eventId, UpdateEventRequest updateEventRequest) throws IOException {
    LOGGER.info("patchEvent called");
    LOGGER.info("fetching existing event: {}", eventId);
    Event event = calendar.events().get(calendarId, eventId).execute();

    if (updateEventRequest.summary() != null && !updateEventRequest.summary().isEmpty()) {
      event.setSummary(updateEventRequest.summary());
    }
    if (updateEventRequest.start() != null) {
      event.setStart(toEventDateTime(updateEventRequest.start()));
    }
    if (updateEventRequest.end() != null) {
      event.setEnd(toEventDateTime(updateEventRequest.end()));
    }

    if (updateEventRequest.visibility() != null) {
      event.setVisibility(updateEventRequest.visibility());
    }
    if (updateEventRequest.description() != null) {
      event.setDescription(updateEventRequest.description());
    }
    Set<EventAttendee> finalAttendees = new HashSet<>(event.getAttendees());
    if (updateEventRequest.attendeesToRemove() != null) {
      Set<String> emailsToRemove =
          updateEventRequest.attendeesToRemove().stream()
              .map(Attendee::email)
              .collect(Collectors.toCollection(HashSet::new));
      finalAttendees =
          finalAttendees.stream()
              .filter(eventAttendee -> !emailsToRemove.contains(eventAttendee.getEmail()))
              .collect(Collectors.toCollection(HashSet::new));
    }
    if (updateEventRequest.attendeesToAdd() != null) {
      List<EventAttendee> attendeesToAdd =
          updateEventRequest.attendeesToAdd().stream()
              .map(
                  attendees ->
                      new EventAttendee()
                          .setEmail(attendees.email())
                          .setOptional(attendees.optional()))
              .toList();
      finalAttendees.addAll(attendeesToAdd);
    }
    event.setAttendees(new ArrayList<>(finalAttendees));
    if (updateEventRequest.guestsCanInviteOthers() != null) {
      event.setGuestsCanInviteOthers(updateEventRequest.guestsCanInviteOthers());
    }
    if (updateEventRequest.guestsCanModify() != null) {
      event.setGuestsCanModify(updateEventRequest.guestsCanModify());
    }
    if (updateEventRequest.guestsCanSeeOtherGuests() != null) {
      event.setGuestsCanSeeOtherGuests(updateEventRequest.guestsCanSeeOtherGuests());
    }

    if (updateEventRequest.location() != null) {
      event.setLocation(updateEventRequest.location());
    }

    if (updateEventRequest.recurrence() != null) {
      event.setRecurrence(updateEventRequest.recurrence());
    }
    Event.Reminders reminders = new Event.Reminders();
    if (updateEventRequest.remindersOverrides() != null) {
      reminders.setOverrides(
          updateEventRequest.remindersOverrides().stream()
              .map(
                  remindersOverrides ->
                      new EventReminder()
                          .setMethod(remindersOverrides.method())
                          .setMinutes(remindersOverrides.minutes()))
              .toList());
      reminders.setUseDefault(false);
    } else {
      reminders.setUseDefault(true);
    }
    event.setReminders(reminders);
    LOGGER.info("patching event: {}", eventId);
    return toEventItem(calendar.events().update(calendarId, eventId, event).execute());
  }

  private com.commoncoder.calendar.ai.agent.tools.model.DateTime toDateTime(
      @Nullable EventDateTime eventDateTime) {
    if (eventDateTime == null) {
      return null;
    }
    return new com.commoncoder.calendar.ai.agent.tools.model.DateTime(
        Optional.ofNullable(eventDateTime.getDate()).map(DateTime::toStringRfc3339).orElse(null),
        Optional.ofNullable(eventDateTime.getDateTime())
            .map(DateTime::toStringRfc3339)
            .orElse(null),
        eventDateTime.getTimeZone());
  }

  private EventDateTime toEventDateTime(
      @Nullable com.commoncoder.calendar.ai.agent.tools.model.DateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    EventDateTime eventDateTime = new EventDateTime();
    if (dateTime.dateTime() != null && !dateTime.dateTime().isEmpty()) {
      eventDateTime.setDateTime(DateTime.parseRfc3339(dateTime.dateTime()));
    }
    if (dateTime.date() != null && !dateTime.date().isEmpty()) {
      eventDateTime.setDate(DateTime.parseRfc3339(dateTime.date()));
    }
    if (dateTime.timeZone() != null && !dateTime.timeZone().isEmpty()) {
      eventDateTime.setTimeZone(dateTime.timeZone());
    }
    return eventDateTime;
  }

  private EventsResponse toEventsResponse(@Nullable Events events) {
    if (events == null) {
      return null;
    }
    List<EventItem> eventItems = events.getItems().stream().map(this::toEventItem).toList();
    events.getItems().clear();
    return new EventsResponse(events, eventItems);
  }

  private EventItem toEventItem(@Nullable Event event) {
    if (event == null) {
      return null;
    }
    com.commoncoder.calendar.ai.agent.tools.model.DateTime start = toDateTime(event.getStart());
    event.setStart(null);
    com.commoncoder.calendar.ai.agent.tools.model.DateTime originalStart =
        toDateTime(event.getOriginalStartTime());
    event.setOriginalStartTime(null);
    com.commoncoder.calendar.ai.agent.tools.model.DateTime end = toDateTime(event.getEnd());
    event.setEnd(null);
    String created = Optional.ofNullable(event.getCreated()).map(String::valueOf).orElse(null);
    event.setCreated(null);
    String updated = Optional.ofNullable(event.getUpdated()).map(String::valueOf).orElse(null);
    event.setUpdated(null);
    return new EventItem(event, start, originalStart, end, created, updated);
  }
}
