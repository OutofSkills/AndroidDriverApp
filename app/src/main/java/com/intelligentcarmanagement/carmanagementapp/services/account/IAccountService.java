package com.intelligentcarmanagement.carmanagementapp.services.account;

import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.models.account.ChangePasswordDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAccountService {
    @POST("/api/DriversAccount/password")
    Call<ChangePasswordDTO> changePassword(@Body ChangePasswordDTO passwordDTO);
}
