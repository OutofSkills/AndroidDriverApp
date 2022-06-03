package com.intelligentcarmanagement.carmanagementapp.api.users.responses;

import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IAvailableResponse {
    void onResponse(boolean response);
    void onFailure(Throwable t);
}
