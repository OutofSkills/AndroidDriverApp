package com.intelligentcarmanagement.carmanagementapp.api.rides.responses;

import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

import java.util.ArrayList;

public interface IGetRidesHistory {
    void onResponse(ArrayList<Ride> historyRides);
    void onFailure(Throwable t);
}
