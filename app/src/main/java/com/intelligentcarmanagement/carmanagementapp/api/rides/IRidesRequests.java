package com.intelligentcarmanagement.carmanagementapp.api.rides;

import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRidesRequests {
    @GET("/api/Rides/driver")
    Call<ArrayList<Ride>> getRides(@Header("authorization") String token, @Query("id") int userId);

    @GET("/api/Rides/ongoing")
    Call<Ride> getOngoingRide(@Header("authorization") String token, @Query("id") int userId);

    @GET("/api/Rides/id")
    Call<Ride> getRideById(@Header("authorization") String token, @Query("id") int rideId);

    @POST("/api/Rides/confirm")
    Call<Ride> startRide(@Header("authorization") String token, @Query("id") int rideId);

    @POST("/api/Rides/end")
    Call<Ride> endRide(@Header("authorization") String token, @Query("id") int rideId);
}
