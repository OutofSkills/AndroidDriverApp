package com.intelligentcarmanagement.carmanagementapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {
    private String title;
    private String body;
    private String dateTime;

    private NotificationCategory notificationCategory;

    public Notification(String title, String body, String dateTime, NotificationCategory notificationCategory) {
        this.title = title;
        this.body = body;
        this.dateTime = dateTime;
        this.notificationCategory = notificationCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateTime() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH).parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public NotificationCategory getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(NotificationCategory notificationCategory) {
        this.notificationCategory = notificationCategory;
    }
}
