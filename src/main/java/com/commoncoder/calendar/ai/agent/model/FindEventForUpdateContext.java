package com.commoncoder.calendar.ai.agent.model;

public record FindEventForUpdateContext(
    String timezone,
    String timeMin,
    String timeMax,
    String freeTextSearchTerms,
    String derivationLogicMetaOutput) {}
