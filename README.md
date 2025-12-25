# calendar-ai-agent

AI agent to assist with managing calendar(s)

## Calendar API Setup

### Finalized Scopes

```
https://www.googleapis.com/auth/userinfo.email
https://www.googleapis.com/auth/calendar.calendarlist.readonly
https://www.googleapis.com/auth/calendar.events.freebusy
https://www.googleapis.com/auth/calendar.settings.readonly
https://www.googleapis.com/auth/calendar.readonly
https://www.googleapis.com/auth/calendar.events
```

## App Local Setup

### Set environment variables

CALENDAR_AI_AGENT_CLIENT_ID=replace_this_fake_client_id;CALENDAR_AI_AGENT_CLIENT_SECRET=replace_this_fake_client_secret

## Google Calendar API: Postman Setup & Testing Guide

This guide outlines the steps to authenticate and test Google Calendar API endpoints using Postman via OAuth 2.0.

---

### 1. Google Cloud Platform (GCP) Configuration

Before using Postman, ensure the following is set up in your [GCP Console](https://console.cloud.google.com/):

1. **Enable API:** Search for "Google Calendar API" and click **Enable**.
2. **OAuth Consent Screen:** Set the user type to **External** and add your email as a **Test User**.
3. **Credentials:**
    * Create an **OAuth 2.0 Client ID** (Web Application).
    * **Crucial:** Add the following to **Authorized redirect URIs**:
      `https://oauth.pstmn.io/v1/callback`

---

### 2. Postman Authorization Setup

Create a new request in Postman and navigate to the **Authorization** tab.

### Configuration Settings

| Field                | Value                                                                                                                                                                                                                                                                                                                               |
|:---------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Type**             | `OAuth 2.0`                                                                                                                                                                                                                                                                                                                         |
| **Add auth data to** | `Request Headers`                                                                                                                                                                                                                                                                                                                   |
| **Grant Type**       | `Authorization Code`                                                                                                                                                                                                                                                                                                                |
| **Callback URL**     | `https://oauth.pstmn.io/v1/callback` (Check "Authorize using browser")                                                                                                                                                                                                                                                              |
| **Auth URL**         | `https://accounts.google.com/o/oauth2/v2/auth`                                                                                                                                                                                                                                                                                      |
| **Access Token URL** | `https://oauth2.googleapis.com/token`                                                                                                                                                                                                                                                                                               |
| **Client ID**        | *Your GCP Client ID*                                                                                                                                                                                                                                                                                                                |
| **Client Secret**    | *Your GCP Client Secret*                                                                                                                                                                                                                                                                                                            |
| **Scope**            | https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/calendar.calendarlist.readonly https://www.googleapis.com/auth/calendar.events.freebusy https://www.googleapis.com/auth/calendar.settings.readonly https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/calendar.events |

---

## 3. Generating the Token

1. Click **Get New Access Token**.
2. Complete the login flow in your browser.
    * *Note: If prompted with "Google hasn't verified this app," click **Advanced** -> **Go to [App Name] (unsafe)**.*
3. In Postman, click **Use Token**.