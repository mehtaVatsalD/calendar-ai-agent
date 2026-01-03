package com.commoncoder.calendar.ai.agent.tools.impl;

import com.commoncoder.calendar.ai.agent.tools.CalendarListTools;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import java.io.IOException;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class CalendarListToolsImpl implements CalendarListTools {

  private static final Logger LOGGER = LoggerFactory.getLogger(CalendarListToolsImpl.class);

  private final Calendar calendar;

  @Autowired
  public CalendarListToolsImpl(Calendar calendar) {
    this.calendar = calendar;
  }

  @Override
  public CalendarList listCalendarList(
      @Nullable Integer maxResults,
      @Nullable String minAccessRole,
      @Nullable String pageToken,
      @Nullable Boolean showDeleted,
      @Nullable Boolean showHidden,
      @Nullable String syncToken)
      throws IOException {
    LOGGER.info("listCalendarList called");
    Calendar.CalendarList.List list = calendar.calendarList().list();
    if (maxResults != null && maxResults > 0) {
      list.setMaxResults(maxResults);
    }
    if (minAccessRole != null && !minAccessRole.isEmpty()) {
      list.setMinAccessRole(minAccessRole);
    }
    if (pageToken != null && !pageToken.isEmpty()) {
      list.setPageToken(pageToken);
    }
    if (showDeleted != null && showDeleted) {
      list.setShowDeleted(true);
    }
    if (showHidden != null && showHidden) {
      list.setShowHidden(true);
    }
    if (syncToken != null && !syncToken.isEmpty()) {
      list.setSyncToken(syncToken);
    }
    return list.execute();
  }

  public CalendarListEntry getCalendarListEntry(String calendarId) throws IOException {
    LOGGER.info("getCalendarListEntry called for: {}", calendarId);
    return calendar.calendarList().get(calendarId).execute();
  }
}
