package com.intelligentcarmanagement.carmanagementapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginResponse;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginService;
import com.intelligentcarmanagement.carmanagementapp.services.users.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.services.users.IUpdateUserResponse;
import com.intelligentcarmanagement.carmanagementapp.services.users.IUsersService;

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

    public void getUserByEmail(String email, IGetUserResponse getUserResponse) {
        IUsersService usersService = RetrofitService.getRetrofit().create(IUsersService.class);
        Call<User> initRequest = usersService.getUserByEmail(email);

        initRequest.enqueue(new Callback<User>() {

            @Override
            public void onResponse(@NonNull Call<User> call, Response<User> response) {
                Log.d("Repo", "Body: " + response.body());
                if(response.isSuccessful()) {
                    Log.d("Repo", "Body: " + response.body());
                    getUserResponse.onResponse(response.body());
                }
                else
                {
                    getUserResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Repo", "Exception: " + t.getMessage());
                getUserResponse.onFailure(t);
            }
        });
    }

    public void updateUser(int id, User user, IUpdateUserResponse updateUserResponse)
    {
        IUsersService usersService = RetrofitService.getRetrofit().create(IUsersService.class);
        Call<User> initRequest = usersService.updateUser(id, user);

        initRequest.enqueue(new Callback<User>() {

            @Override
            public void onResponse(@NonNull Call<User> call, Response<User> response) {
                Log.d("Repo", "Body: " + response.body());
                if(response.isSuccessful()) {
                    Log.d("Repo", "Body: " + response.body());
                    updateUserResponse.onResponse(response.body());
                }
                else
                {
                    updateUserResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Repo", "Exception: " + t.getMessage());
                updateUserResponse.onFailure(t);
            }
        });
    }
}
