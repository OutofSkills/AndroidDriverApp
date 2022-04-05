package com.intelligentcarmanagement.carmanagementapp.services.users;


import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IGetUserResponse {
    void onResponse(User userResponse);
    void onFailure(Throwable t);
}
