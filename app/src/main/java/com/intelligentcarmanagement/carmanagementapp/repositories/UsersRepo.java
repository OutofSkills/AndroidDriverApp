package com.intelligentcarmanagement.carmanagementapp.repositories;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginResponse;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRepo {

    public void loginRemote(LoginRequest loginRequest, ILoginResponse loginResponse) {
        ILoginService loginService = RetrofitService.getRetrofit().create(ILoginService.class);
        Call<LoginResponse> initLogin = loginService.login(loginRequest);

        initLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    loginResponse.onResponse(response.body());
                }
                else
                {
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponse.onFailure(t);
            }
        });
    }
}
