package com.intelligentcarmanagement.carmanagementapp.api.users;

import com.intelligentcarmanagement.carmanagementapp.models.User;

public interface IMakeAvailableResponse {
    void onResponse(boolean response);
    void onFailure(Throwable t);
}
