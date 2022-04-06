package com.intelligentcarmanagement.carmanagementapp.services.users;

import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IUsersService {
    @GET("/api/Users/byEmail")
    Call<User> getUserByEmail(@Query("email") String email);

    @PUT("/api/Users")
    Call<User> updateUser(@Query("id") int id, @Body User user);
}
