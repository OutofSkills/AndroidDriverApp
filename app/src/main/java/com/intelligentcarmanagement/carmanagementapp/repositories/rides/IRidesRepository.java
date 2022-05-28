package com.intelligentcarmanagement.carmanagementapp.repositories.rides;


import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetOngoingRide;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;

public interface IRidesRepository {
    public void getRides(String token, int userId, IGetRidesHistory getRidesHistory);
    public void getOngoingRide(String token, int userId, IGetOngoingRide getOngoingRide);
}
