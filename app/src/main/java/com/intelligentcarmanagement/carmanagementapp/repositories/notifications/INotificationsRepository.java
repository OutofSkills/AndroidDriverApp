package com.intelligentcarmanagement.carmanagementapp.repositories.notifications;


import com.intelligentcarmanagement.carmanagementapp.api.notifications.responses.IGetNotifications;
import com.intelligentcarmanagement.carmanagementapp.api.notifications.responses.IUpdateToken;

public interface INotificationsRepository {
    public void updateToken(String token, String id, String firebaseToken, IUpdateToken updateTokenResponse);
    public void getNotifications(String token, String id, IGetNotifications getNotifications);
}
