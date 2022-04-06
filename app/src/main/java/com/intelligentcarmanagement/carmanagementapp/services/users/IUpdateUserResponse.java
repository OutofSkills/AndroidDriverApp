package com.intelligentcarmanagement.carmanagementapp.services.users;

import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IUpdateUserResponse {
    void onFailure(Throwable throwable);

    void onResponse(User user);
}
