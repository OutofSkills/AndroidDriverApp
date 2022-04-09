package com.intelligentcarmanagement.carmanagementapp.services.login;

import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ILoginService {
    @POST("/api/DriversAccount/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
