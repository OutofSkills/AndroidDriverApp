package com.intelligentcarmanagement.carmanagementapp.repositories.reviews;

import com.intelligentcarmanagement.carmanagementapp.api.reviews.IRateResponse;

public interface IReviewsRepository {
    void rateClient(String token, int rideId, double rating, IRateResponse rateClientResponse);

    void evaluateRide(String token, int rideId, double accuracy, IRateResponse rateRideResponse);
}
