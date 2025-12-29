package com.commoncoder.calendar.ai.agent.controller;

import com.commoncoder.calendar.ai.agent.tools.CalendarListService;
import com.commoncoder.calendar.ai.agent.tools.EventsService;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class TimeService {

  @Tool(
      name = "get_current_datetime",
      description = "Helps in getting current time given a timezone.")
  public String getCurrentTime(
      @ToolParam(description = "Timezone for which current time is to be queried.")
          String timezone) {
    ZoneId zoneId = ZoneId.of(timezone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    System.out.println(now.format(formatter));
    return now.format(formatter);
  }

  @Tool(name = "get_current_date", description = "Helps in getting current date given a timezone.")
  public String getCurrentDate(
      @ToolParam(description = "Timezone for which current date is to be queried.")
          String timezone) {
    ZoneId zoneId = ZoneId.of(timezone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println(now.format(formatter));
    return now.format(formatter);
  }

  public record DateTime(Long value, Boolean dateOnly, Integer timeZoneShift) {}

  @Tool(
      name = "datetime_to_user_readable",
      description =
          "Converts datetime objects containing dateTime value, dateOnly, timeZoneShift to strings which are user readable.")
  public List<String> datetimeToUserReadable(
      @ToolParam(
              description =
                  "List of objects containing datetime or date in epoch milliseconds, dateOnly field and timeZoneShift fields to be converted into user readable strings")
          List<DateTime> dateTimes) {
    System.out.println("datetimeToUserReadable");
    return parseRfc3339(
        dateTimes.stream()
            .map(
                dateTime ->
                    new com.google.api.client.util.DateTime(
                            dateTime.dateOnly(), dateTime.value(), dateTime.timeZoneShift())
                        .toStringRfc3339())
            .toList());
  }

  List<String> parseRfc3339(List<String> rfc3339Strings) {
    System.out.println("rfc3339: " + rfc3339Strings.toString());
    return rfc3339Strings.stream()
        .map(
            rfc3339String -> {
              // 1. Parse the RFC 3339 string
              // This handles 'Z' or +/- offsets automatically
              OffsetDateTime offsetDateTime = OffsetDateTime.parse(rfc3339String);

              // 2. Format the Date: [Full Month Name] [Day], [Year]
              DateTimeFormatter dateFormatter =
                  DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
              String datePart = offsetDateTime.toLocalDateTime().format(dateFormatter);

              // 3. Format the Time: [12-hour clock with AM/PM]
              // This extracts exactly what is between 'T' and the offset
              DateTimeFormatter timeFormatter =
                  DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
              String timePart = offsetDateTime.toLocalDateTime().format(timeFormatter);

              // 4. Handle the Timezone description
              String offsetId = offsetDateTime.getOffset().getId();
              String tzDescription;

              if (offsetId.equals("Z") || offsetId.equals("+00:00") || offsetId.equals("-00:00")) {
                tzDescription = "UTC (Coordinated Universal Time)";
              } else {
                int totalSeconds = offsetDateTime.getOffset().getTotalSeconds();
                int hours = Math.abs(totalSeconds) / 3600;
                int minutes = (Math.abs(totalSeconds) % 3600) / 60;
                String direction = totalSeconds >= 0 ? "ahead of" : "behind";
                tzDescription =
                    String.format(
                        "%s (%d hours and %d minutes %s UTC)", offsetId, hours, minutes, direction);
              }

              // 5. Build final string
              return String.format(
                  "**Date:** %s\n**Time:** %s\n**Timezone:** %s",
                  datePart, timePart, tzDescription);
            })
        .toList();
  }
}

@RestController
public class SampleAIController {

  static final String CALENDAR_MANAGEMENT_SYSTEM_PROMPT =
      """
          # ROLE
          You are a highly efficient Calendar Management Agent. You specialize in the Google Calendar API and help users organize their time with technical precision and minimal friction.

          # SCOPE & GUARDRAILS
          - **Primary Domain:** Only handle requests related to calendar events, calendar lists, and scheduling.
          - **Out of Scope:** Politely decline any requests that do not involve managing the user's schedule or calendar metadata.

          # OPERATIONAL PROTOCOL
          1. **Implicit Execution:** Unless the user explicitly asks for a plan or confirmation, execute the necessary tools immediately.
          2. Internally identify and output the sequence of planned tasks that you will execute abd then execute them.
          3. **Timezone:** If the user has not provided any specific timezone, ALWAYS get the timezone of user's primary calendar using 'get_user_calendar_list_entry' to answer any availability queries, listing events or creating any new event.
          You have to use correct timezone to fetch the data and not convert the out to expected timezone yourself.
          4. **Dates and time**:
          5. When checking availability, always set `singleEvents=true`. This is critical to ensure recurring events are expanded into individual instances to identify true overlaps.
          6. **Reminders** When creating an event, don't add any reminders overrides, unless asked explicitly by users. Even if asked by users, check that they are not default as default values can be set in overrides.
          7. When asked for scheduling/creating new events, Please create new event, and  don't check for list existing events unless you are asked to check for user's availability. You can setup meeting at requested time even if user is busy at that time if explicitly asked by the user.
          8. You should always fetch current date/time and then derive dates for queries like today,yesterday,tomorrow,day after tomorrow, day before yesterday, next week etc.
          9. For creating recurring event, first create the recurrence rules and internally check, reflect and think if they are valid as per knowledge given below. If not retry and create valid rules.
          10.Time range should be valid for creating events i.e. end time should be greater than start time. Internally check, reflect and think if they are valid. Sometimes user can be multi day events like starting 11pm and last 2 hours. so it ends at 1am next day.
          11. You should use event's 'start' and 'end' to know start and end of the event. If 'dateTime' if present, you should user 'dateTime' else considering event to be full day event you should use 'date' for knowing interval of the event.
          When summarizing the event you should provide both start and end intervals.
          For outputting event's start and end datetime or date objects to user readable string, ALWAYS use 'datetime_to_user_readable' tool and don't convert it yourself.
          122. Before responding final response to user, ALWAYS collect all the datetime/date objects and send it to 'datetime_to_user_readable' tool to convert it to user-readable strings.

          # TECHNICAL SPECIFICATIONS
          - **Calendar vs. List:** - Use `CalendarList` for user-specific UI settings (colors, hidden calendars).
              - Use `Calendars` for global metadata.

          # RECURRENCE RULES (RFC 5545)
          Recurring events
          Some events occur multiple times on a regular schedule, such as weekly meetings, birthdays, and holidays. Other than having different start and end times, these repeated events are often identical.
          Events are called recurring if they repeat according to a defined schedule. Single events are non-recurring and happen only once.
          You can create recurring events like single events by defining recurrence list.
          The schedule for a recurring event is defined in two parts:
          Its start and end fields (which define the first occurrence, as if this were just a stand-alone single event), and
          Its recurrence field (which defines how the event should be repeated over time).

          The RRULE property (prefixed by 'RRULE:') is the most important as it defines a regular rule for repeating the event. It is composed of several components. Some of them are:
            - FREQ — The frequency with which the event should be repeated (such as DAILY or WEEKLY). Required.
            - INTERVAL — Works together with FREQ to specify how often the event should be repeated. For example, FREQ=DAILY;INTERVAL=2 means once every two days.
            - UNTIL — The date or date-time until which the event should be repeated (inclusive).
            - COUNT — Number of times this event should be repeated. This is NOT number of DAYS but number of occurrence of meetings!
            (You **should not use both UNTIL AND COUNT** in the same rule. Prefer using UNTIL over COUNT until necessarily needed for satisfying user's query.
            Determine the end condition: Is it a specific date or a number of occurrences?
            IF Date: Use UNTIL (format: YYYYMMDD) and OMIT COUNT.
            IF Occurrences: Use COUNT (integer) and OMIT UNTIL.)
            - BYDAY — Days of the week on which the event should be repeated (SU, MO, TU, WE, TH, FR, SA). Other similar components include BYMONTH, BYYEARDAY, and BYHOUR.

          The RDATE property (prefixed by 'RDATE:') specifies additional dates or date-times when the event occurrences should happen. For example, RDATE;VALUE=DATE:19970101,19970120. Use this to add extra occurrences not covered by the RRULE.
          The EXDATE property (prefixed by 'EXDATE:') is similar to RDATE, but specifies dates or date-times when the event should not happen. That is, those occurrences should be excluded. This must point to a valid instance generated by the recurrence rule.
          EXDATE and RDATE can have a time zone, and must be dates (not date-times) for all-day events.

          Each of the properties may occur within the recurrence field multiple times. The recurrence is defined as the union of all RRULE and RDATE rules, minus the ones excluded by all EXDATE rules.

          Here are some examples of recurrent events:
          An event that happens from 6am until 7am every Tuesday and Friday starting from September 15th, 2015 and stopping after the fifth occurrence on September 29th:
          ...
          "start": {
           "dateTime": "2015-09-15T06:00:00+02:00",
           "timeZone": "Europe/Zurich"
          },
          "end": {
           "dateTime": "2015-09-15T07:00:00+02:00",
           "timeZone": "Europe/Zurich"
          },
          "recurrence": [
           "RRULE:FREQ=WEEKLY;COUNT=5;BYDAY=TU,FR"
          ],
          …
          An all-day event starting on June 1st, 2015 and repeating every 3 days throughout the month, excluding June 10th but including June 9th and 11th:
          ...
          "start": {
           "date": "2015-06-01"
          },
          "end": {
           "date": "2015-06-02"
          },
          "recurrence": [
           "EXDATE;VALUE=DATE:20150610",
           "RDATE;VALUE=DATE:20150609,20150611",
           "RRULE:FREQ=DAILY;UNTIL=20150628;INTERVAL=3"
          ],
          …

          Following is Invalid and SHOULD NOT be used, as it specifies both UNTIL and count in same RRULE
          …
          "recurrence": [
             "RRULE:FREQ=WEEKLY;UNTIL=20261231;COUNT=52"
          ],
          …
          """;

  private final ChatClient.Builder clientBuilder;
  private final CalendarListService calendarListService;
  private final EventsService eventsService;

  @Autowired
  public SampleAIController(
      ChatClient.Builder clientBuilder,
      CalendarListService calendarListService,
      EventsService eventsService) {
    this.clientBuilder = clientBuilder;
    this.calendarListService = calendarListService;
    this.eventsService = eventsService;
  }

  @GetMapping("/ai")
  public String getAIResponse(@RequestParam String q) {
    ChatClient client = clientBuilder.build();
    var v =
        client
            .prompt()
            .system(CALENDAR_MANAGEMENT_SYSTEM_PROMPT)
            .user(q)
            .tools(new TimeService(), calendarListService, eventsService)
            .advisors(new SimpleLoggerAdvisor())
            .call();
    return v.content();
  }
}
