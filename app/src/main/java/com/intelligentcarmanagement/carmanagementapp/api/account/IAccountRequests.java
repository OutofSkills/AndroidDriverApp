package com.intelligentcarmanagement.carmanagementapp.api.account;

import com.intelligentcarmanagement.carmanagementapp.models.account.ChangePasswordDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAccountRequests {
    @POST("/api/DriversAccount/password")
    Call<ChangePasswordDTO> changePassword(@Body ChangePasswordDTO passwordDTO);
}
