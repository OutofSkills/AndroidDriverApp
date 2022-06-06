package com.intelligentcarmanagement.carmanagementapp.api.reviews;

import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

public interface IRateResponse {
    void onResponse();
    void onFailure(Throwable t);
}
