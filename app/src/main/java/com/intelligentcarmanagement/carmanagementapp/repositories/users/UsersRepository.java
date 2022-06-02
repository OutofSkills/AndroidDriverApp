package com.intelligentcarmanagement.carmanagementapp.repositories.users;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IMakeAvailableResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IUpdateLocation;
import com.intelligentcarmanagement.carmanagementapp.models.login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerErrorResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerValidationError;
import com.intelligentcarmanagement.carmanagementapp.api.login.ILoginResponse;
import com.intelligentcarmanagement.carmanagementapp.api.login.ILoginRequests;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IUpdateUserResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.IUsersRequests;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRepository implements IUsersRepository{

    private static final String TAG = "UsersRepository";

    public void loginRemote(LoginRequest loginRequest, ILoginResponse loginResponse) {
        ILoginRequests loginService = RetrofitService.getRetrofit().create(ILoginRequests.class);
        Call<LoginResponse> initLogin = loginService.login(loginRequest);

        initLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()) {
                    loginResponse.onResponse(response.body());
                }
                else {
                    // TODO: user error codes from HTTP
                    // 400 for validation
                    // 500 for server errors
                    int responseErrorCode = (400 == response.code()) ? 0 :
                            (401 <= response.code() && response.code() < 500) ? 1 :
                                    (500 <= response.code() && response.code() <= 600) ? 2 : 3;

                    try {
                        Gson gson = new Gson();
                        switch (responseErrorCode) {
                            case 0: // Validation error response
                                Log.d(TAG, "Code: " + response.code() + " validation error.");
                                ServerValidationError errorValidationResponse = gson.fromJson(response.errorBody().string(), ServerValidationError.class);
                                loginResponse.onServerValidationFailure(errorValidationResponse);
                                break;
                            case 1:
                                Log.d(TAG, "Code: " + response.code() + " client error.");
                            case 2:
                                Log.d(TAG, "Code: " + response.code() + " server error.");
                                ServerErrorResponse serverErrorResponse = gson.fromJson(response.errorBody().string(), ServerErrorResponse.class);
                                loginResponse.onServerFailure(serverErrorResponse);
                                break;
                            case 3:
                                Log.d(TAG, "Code: " + response.code() + " other error.");
                                loginResponse.onServerFailure(new ServerErrorResponse("Server error!", "Unknown"));
                                break;
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Response parse exception: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponse.onFailure(t);
            }
        });
    }

    public void getUserByEmail(String email, IGetUserResponse getUserResponse) {
        IUsersRequests usersService = RetrofitService.getRetrofit().create(IUsersRequests.class);
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
        IUsersRequests usersService = RetrofitService.getRetrofit().create(IUsersRequests.class);
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

    public void makeAvailable(int id, boolean isAvailable, IMakeAvailableResponse makeAvailableResponse)
    {
        IUsersRequests usersService = RetrofitService.getRetrofit().create(IUsersRequests.class);
        Call<Boolean> initRequest = usersService.makeAvailable(id, isAvailable);

        initRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful())
                {
                    makeAvailableResponse.onResponse(response.body());
                }
                else
                {
                    try {
                        makeAvailableResponse.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                makeAvailableResponse.onFailure(t);
            }
        });
    }

    @Override
    public void updateLocation(String token, int id, String latitude, String longitude, IUpdateLocation updateLocationResponse) {
        IUsersRequests usersRequests = RetrofitService.getRetrofit().create(IUsersRequests.class);
        Call<Void> initRequest = usersRequests.updateLocation(token, id, latitude, longitude);

        initRequest.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                updateLocationResponse.onResponse();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                updateLocationResponse.onFailure(t);
            }
        });
    }
}
