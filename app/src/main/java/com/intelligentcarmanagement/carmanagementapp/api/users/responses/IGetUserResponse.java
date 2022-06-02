package com.intelligentcarmanagement.carmanagementapp.api.users.responses;


import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IGetUserResponse {
    void onResponse(User userResponse);
    void onFailure(Throwable t);
}
