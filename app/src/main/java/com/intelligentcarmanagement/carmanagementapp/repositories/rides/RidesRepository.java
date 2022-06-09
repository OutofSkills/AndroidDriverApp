package com.intelligentcarmanagement.carmanagementapp.repositories.rides;

import android.util.Log;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.api.rides.IRidesRequests;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRide;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;

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

    @Override
    public void getRide(String token, int rideId, IGetRide getRide) {
        IRidesRequests ridesRequest = RetrofitService.getRetrofit().create(IRidesRequests.class);
        Call<Ride> initRequest = ridesRequest.getRideById(token, rideId);

        initRequest.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                if(response.isSuccessful())
                    getRide.onResponse(response.body());
                else {
                    try {
                        getRide.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: ");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                getRide.onFailure(t);
            }
        });
    }

    @Override
    public void getOngoingRide(String token, int userId, IGetRide getOngoingRide) {
        IRidesRequests ridesRequest = RetrofitService.getRetrofit().create(IRidesRequests.class);
        Call<Ride> initRequest = ridesRequest.getOngoingRide(token, userId);

        initRequest.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                if(response.isSuccessful())
                    getOngoingRide.onResponse(response.body());
                else {
                    try {
                        getOngoingRide.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: ");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                getOngoingRide.onFailure(t);
            }
        });
    }

    @Override
    public void startRide(String token, int rideId, IGetRide getRide) {
        IRidesRequests ridesRequest = RetrofitService.getRetrofit().create(IRidesRequests.class);
        Call<Ride> initRequest = ridesRequest.startRide(token, rideId);

        initRequest.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                if(response.isSuccessful())
                    getRide.onResponse(response.body());
                else {
                    try {
                        getRide.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: ");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                getRide.onFailure(t);
            }
        });
    }

    @Override
    public void endRide(String token, int rideId, IGetRide getRide) {
        IRidesRequests ridesRequest = RetrofitService.getRetrofit().create(IRidesRequests.class);
        Call<Ride> initRequest = ridesRequest.endRide(token, rideId);

        initRequest.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                if(response.isSuccessful())
                    if(response.isSuccessful())
                        getRide.onResponse(response.body());
                    else {
                        try {
                            getRide.onFailure(new Throwable(response.errorBody().string()));
                        } catch (IOException e) {
                            Log.d(TAG, "onResponse: ");
                            e.printStackTrace();
                        }
                    }
                else {
                    try {
                        getRide.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: ");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                getRide.onFailure(t);
            }
        });
    }
}
