package com.intelligentcarmanagement.carmanagementapp.services.account;

import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerErrorResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerValidationError;
import com.intelligentcarmanagement.carmanagementapp.models.account.ChangePasswordDTO;

public interface IPasswordChangeResponse {
    void onResponse(ChangePasswordDTO passwordResponse);
    void onServerFailure(ServerErrorResponse error);
    void onServerValidationFailure(ServerValidationError error);
    void onFailure(Throwable t);
}
