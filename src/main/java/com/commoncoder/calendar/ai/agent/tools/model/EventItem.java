package com.commoncoder.calendar.ai.agent.tools.model;

import com.google.api.services.calendar.model.Event;

public record EventItem(
    Event event,
    DateTime startInRfc3339,
    DateTime originalStartInRfc3339,
    DateTime endInRfc3339,
    String createdInRfc3339,
    String updatedInRfc3339) {}
