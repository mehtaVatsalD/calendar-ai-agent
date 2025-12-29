package com.commoncoder.calendar.ai.agent.tools.response;

import com.commoncoder.calendar.ai.agent.tools.model.EventItem;
import com.google.api.services.calendar.model.Events;
import java.util.List;

public record EventsResponse(Events eventsMetaData, List<EventItem> eventItems) {}
