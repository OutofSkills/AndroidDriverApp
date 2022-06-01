package com.intelligentcarmanagement.carmanagementapp.api.notifications.responses;

import com.intelligentcarmanagement.carmanagementapp.models.notifications.Notification;

import java.util.ArrayList;

public interface IGetNotifications {
    void onFailure(Throwable throwable);

    void onResponse(ArrayList<Notification> notifications);
}
