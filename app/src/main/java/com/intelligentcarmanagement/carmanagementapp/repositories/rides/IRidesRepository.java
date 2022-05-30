package com.intelligentcarmanagement.carmanagementapp.repositories.rides;


import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRide;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;

public interface IRidesRepository {
    public void getRides(String token, int userId, IGetRidesHistory getRidesHistory);
    public void getRide(String token, int rideId, IGetRide getRide);
    public void getOngoingRide(String token, int userId, IGetRide getOngoingRide);
    public void startRide(String token, int rideId, IGetRide getRide);
    public void endRide(String token, int rideId, IGetRide getRide);
}
