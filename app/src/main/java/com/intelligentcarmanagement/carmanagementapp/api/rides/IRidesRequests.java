package com.intelligentcarmanagement.carmanagementapp.api.rides;

import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IRidesRequests {
    @GET("/api/Rides/driver")
    Call<ArrayList<Ride>> getRides(@Header("authorization") String token, @Query("id") int userId);
}
