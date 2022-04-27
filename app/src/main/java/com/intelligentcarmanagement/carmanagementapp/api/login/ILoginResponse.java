package com.intelligentcarmanagement.carmanagementapp.api.login;

import com.intelligentcarmanagement.carmanagementapp.models.login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerErrorResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerValidationError;

public interface ILoginResponse {
    void onResponse(LoginResponse loginResponse);
    void onFailure(Throwable t);

    void onServerValidationFailure(ServerValidationError errorValidationResponse);

    void onServerFailure(ServerErrorResponse serverErrorResponse);
}
