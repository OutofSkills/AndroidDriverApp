package com.intelligentcarmanagement.carmanagementapp.repositories.reviews;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.api.reviews.IRateClient;
import com.intelligentcarmanagement.carmanagementapp.api.reviews.IReviewRequests;
import com.intelligentcarmanagement.carmanagementapp.api.rides.IRidesRequests;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsRepository implements IReviewsRepository{
    @Override
    public void rateClient(String token, int rideId, double rating, IRateClient rateClientResponse) {
        IReviewRequests reviewsRequest = RetrofitService.getRetrofit().create(IReviewRequests.class);
        Call<Void> initRequest = reviewsRequest.rateClient(token, rideId, rating);

        initRequest.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                rateClientResponse.onResponse();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                rateClientResponse.onFailure(t);
            }
        });
    }
}
