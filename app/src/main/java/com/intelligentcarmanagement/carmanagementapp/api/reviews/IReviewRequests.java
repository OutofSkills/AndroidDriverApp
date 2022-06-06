package com.intelligentcarmanagement.carmanagementapp.api.reviews;

import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IReviewRequests {
    @POST("/api/Reviews/client")
    Call<Void> rateClient(@Header("authorization") String token, @Query("rideId") int rideId, @Query("rating") double rating);

    @POST("/api/Reviews/ride/accuracy")
    Call<Void> evaluateRide(@Header("authorization") String token, @Query("rideId") int rideId, @Query("accuracy") double accuracy);
}
