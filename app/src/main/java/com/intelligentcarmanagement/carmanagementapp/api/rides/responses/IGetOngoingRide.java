package com.intelligentcarmanagement.carmanagementapp.api.rides.responses;

import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.util.ArrayList;

public interface IGetOngoingRide {
    void onResponse(Ride ride);
    void onFailure(Throwable t);
}
