package com.commoncoder.calendar.ai.agent.config;

import com.commoncoder.calendar.ai.agent.annotations.CalendarAPIScopes;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfiguration {

  @Value("${calendar.ai-agent.client-id}")
  private String clientId;

  @Value("${calendar.ai-agent.client-secret}")
  private String clientSecret;

  @Bean
  @CalendarAPIScopes
  public List<String> getCalendarAPIScopes() {
    return new ArrayList<>(
        Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/calendar.calendarlist.readonly",
            "https://www.googleapis.com/auth/calendar.events.freebusy",
            "https://www.googleapis.com/auth/calendar.settings.readonly",
            "https://www.googleapis.com/auth/calendar.readonly",
            "https://www.googleapis.com/auth/calendar.events"));
  }

  @Bean
  public HttpTransport getHttpTransport() throws GeneralSecurityException, IOException {
    return GoogleNetHttpTransport.newTrustedTransport();
  }

  @Bean
  public JsonFactory getJsonFactory() {
    return GsonFactory.getDefaultInstance();
  }

  @Bean
  public DataStoreFactory getDataStoreFactory() {
    return MemoryDataStoreFactory.getDefaultInstance();
  }

  @Bean
  public Credential getGoogleAuthCredential(
      HttpTransport httpTransport,
      JsonFactory jsonFactory,
      DataStoreFactory dataStoreFactory,
      @CalendarAPIScopes List<String> calendarAPIScopes)
      throws IOException {
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientId, clientSecret, calendarAPIScopes)
            .setDataStoreFactory(dataStoreFactory)
            .setAccessType("offline")
            .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }
}
