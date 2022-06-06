package com.intelligentcarmanagement.carmanagementapp.repositories.reviews;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.api.reviews.IRateResponse;
import com.intelligentcarmanagement.carmanagementapp.api.reviews.IReviewRequests;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsRepository implements IReviewsRepository{
    @Override
    public void rateClient(String token, int rideId, double rating, IRateResponse rateClientResponse) {
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

    @Override
    public void evaluateRide(String token, int rideId, double accuracy, IRateResponse rateRideResponse) {
        IReviewRequests reviewsRequest = RetrofitService.getRetrofit().create(IReviewRequests.class);
        Call<Void> initRequest = reviewsRequest.evaluateRide(token, rideId, accuracy);

        initRequest.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    rateRideResponse.onResponse();
                }
                else
                {
                    try {
                        rateRideResponse.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                rateRideResponse.onFailure(t);
            }
        });
    }
}
