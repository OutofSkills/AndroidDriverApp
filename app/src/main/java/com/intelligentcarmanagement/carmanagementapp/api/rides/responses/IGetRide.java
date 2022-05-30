package com.intelligentcarmanagement.carmanagementapp.api.rides.responses;

import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

public interface IGetRide {
    void onResponse(Ride ride);
    void onFailure(Throwable t);
}
