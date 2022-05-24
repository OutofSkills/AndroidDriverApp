package com.intelligentcarmanagement.carmanagementapp.repositories.rides;

import android.util.Log;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.api.rides.IRidesRequests;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;
import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidesRepository implements IRidesRepository{
    private static final String TAG = "RidesRepository";

    @Override
    public void getRides(String token, int userId, IGetRidesHistory getRidesHistory) {
        IRidesRequests ridesRequest = RetrofitService.getRetrofit().create(IRidesRequests.class);
        Call<ArrayList<Ride>> initRequest = ridesRequest.getRides(token, userId);

        initRequest.enqueue(new Callback<ArrayList<Ride>>() {
            @Override
            public void onResponse(Call<ArrayList<Ride>> call, Response<ArrayList<Ride>> response) {
                if(response.isSuccessful())
                    getRidesHistory.onResponse(response.body());
                else {
                    try {
                        getRidesHistory.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: ");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Ride>> call, Throwable t) {
                getRidesHistory.onFailure(t);
            }
        });
    }
}
