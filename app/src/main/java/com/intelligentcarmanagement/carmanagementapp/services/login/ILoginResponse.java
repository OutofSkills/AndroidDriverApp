package com.intelligentcarmanagement.carmanagementapp.services.login;

import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;

public interface ILoginResponse {
    void onResponse(LoginResponse loginResponse);
    void onFailure(Throwable t);
}
