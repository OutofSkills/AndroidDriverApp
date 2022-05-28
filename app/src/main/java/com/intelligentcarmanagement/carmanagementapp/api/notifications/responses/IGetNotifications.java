package com.intelligentcarmanagement.carmanagementapp.api.notifications.responses;

import com.intelligentcarmanagement.carmanagementapp.models.Notification;

import java.util.ArrayList;

public interface IGetNotifications {
    void onFailure(Throwable throwable);

    void onResponse(ArrayList<Notification> notifications);
}
