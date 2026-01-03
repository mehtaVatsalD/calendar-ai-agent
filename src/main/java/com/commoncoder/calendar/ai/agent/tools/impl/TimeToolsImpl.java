package com.commoncoder.calendar.ai.agent.tools.impl;

import com.commoncoder.calendar.ai.agent.tools.TimeTools;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class TimeToolsImpl implements TimeTools {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeToolsImpl.class);

  public String getCurrentTime(String timeZone) {
    ZoneId zoneId = ZoneId.of(timeZone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LOGGER.info("Current time at timezone {} is {}", timeZone, now.format(formatter));
    return now.format(formatter);
  }

  public String getCurrentDate(String timeZone) {
    ZoneId zoneId = ZoneId.of(timeZone);
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LOGGER.info("Current date at timezone {} is {}", timeZone, now.format(formatter));
    return now.format(formatter);
  }
}
