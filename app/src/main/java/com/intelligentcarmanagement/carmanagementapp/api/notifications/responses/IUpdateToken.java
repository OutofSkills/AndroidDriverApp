package com.intelligentcarmanagement.carmanagementapp.api.notifications.responses;

public interface IUpdateToken {
    void onFailure(Throwable throwable);

    void onResponse(String newFirebaseToken);
}
