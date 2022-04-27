package com.intelligentcarmanagement.carmanagementapp.api.login;

import com.intelligentcarmanagement.carmanagementapp.models.login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ILoginRequests {
    @POST("/api/DriversAccount/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
