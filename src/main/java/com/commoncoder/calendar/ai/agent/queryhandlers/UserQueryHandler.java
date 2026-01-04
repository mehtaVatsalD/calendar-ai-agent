package com.commoncoder.calendar.ai.agent.queryhandlers;

import com.commoncoder.calendar.ai.agent.model.UserQueryResponse;

public interface UserQueryHandler {
  UserQueryResponse handle(String userQuery);
}
