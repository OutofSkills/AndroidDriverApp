package com.intelligentcarmanagement.carmanagementapp.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerErrorResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerValidationError;
import com.intelligentcarmanagement.carmanagementapp.models.account.ChangePasswordDTO;
import com.intelligentcarmanagement.carmanagementapp.services.account.IAccountService;
import com.intelligentcarmanagement.carmanagementapp.services.account.IPasswordChangeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepo {

    public void changePassword(ChangePasswordDTO passwordDTO, IPasswordChangeResponse passwordChangeResponse) {
        IAccountService accountService = RetrofitService.getRetrofit().create(IAccountService.class);
        Call<ChangePasswordDTO> initLogin = (Call<ChangePasswordDTO>) accountService.changePassword(passwordDTO);

        initLogin.enqueue(new Callback<ChangePasswordDTO>() {
            @Override
            public void onResponse(Call<ChangePasswordDTO> call, Response<ChangePasswordDTO> response) {
                if(response.isSuccessful()) {
                    passwordChangeResponse.onResponse(response.body());
                }
                else
                {
                    // TODO: user error codes from HTTP
                    // 400 for validation
                    // 500 for server errors
                    int responseErrorCode = (400 == response.code()) ? 0 :
                            (401 <= response.code() && response.code() < 500 ) ? 1 :
                            (500 <= response.code() && response.code() <= 600 ) ? 2 : 3;

                    try {
                        Gson gson = new Gson();
                        switch (responseErrorCode) {
                            case 0: // Validation error response
                                Log.d("Repo", "Code: " + response.code() + " validation error.");
                                ServerValidationError errorValidationResponse = gson.fromJson(response.errorBody().string(), ServerValidationError.class);
                                passwordChangeResponse.onServerValidationFailure(errorValidationResponse);
                                break;
                            case 1:
                                Log.d("Repo", "Code: " + response.code() + " client error.");
                            case 2:
                                Log.d("Repo", "Code: " + response.code() + " server error.");
                                ServerErrorResponse serverErrorResponse = gson.fromJson(response.errorBody().string(), ServerErrorResponse.class);
                                passwordChangeResponse.onServerFailure(serverErrorResponse);
                                break;
                            case 3:
                                Log.d("Repo", "Code: " + response.code() + " other error.");
                                passwordChangeResponse.onServerFailure(new ServerErrorResponse("Server error!", "Unknown"));
                                break;
                        }
                    }catch (Exception e){
                        Log.d("Repo", "Response parse exception: " + e.getMessage());
                    }


//                    Gson gson = new Gson();
//                    try
//                    {
//                        ServerErrorResponse serverErrorResponse = gson.fromJson(response.errorBody().string(), ServerErrorResponse.class);
//                        if(serverErrorResponse == null || serverErrorResponse.getErrorCode() == null)
//                        {
//                            ServerValidationError errorValidationResponse = new Gson().fromJson(response.errorBody().string(), ServerValidationError.class);
//                            //Log.d("Repo", "onResponse: " + errorValidationResponse.toString());
//                            passwordChangeResponse.onServerValidationFailure(errorValidationResponse);
//                        }
//                        else
//                        {
//                            Log.d("Repo", "onResponse: " + serverErrorResponse.getErrorCode());
//                            passwordChangeResponse.onServerFailure(serverErrorResponse);
//                        }
//
//                    }catch (Exception e)
//                    {
//                        Log.d("Repo", "onResponse: " + e.getMessage());
//                        passwordChangeResponse.onServerFailure(new ServerErrorResponse("Server error!", "Unknown"));
//                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordDTO> call, Throwable t) {
                passwordChangeResponse.onFailure(t);
            }
        });
    }
}
