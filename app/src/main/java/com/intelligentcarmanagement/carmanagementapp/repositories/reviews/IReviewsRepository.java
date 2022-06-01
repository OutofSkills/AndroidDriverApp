package com.intelligentcarmanagement.carmanagementapp.repositories.reviews;

import com.intelligentcarmanagement.carmanagementapp.api.reviews.IRateClient;

public interface IReviewsRepository {
    void rateClient(String token, int rideId, double rating, IRateClient rateClientResponse);
}
