package com.intelligentcarmanagement.carmanagementapp.api.users.responses;

import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IUpdateUserResponse {
    void onFailure(Throwable throwable);

    void onResponse(User user);
}
