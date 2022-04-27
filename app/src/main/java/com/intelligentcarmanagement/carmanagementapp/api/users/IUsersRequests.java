package com.intelligentcarmanagement.carmanagementapp.api.users;

import com.intelligentcarmanagement.carmanagementapp.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IUsersRequests {
    @GET("/api/Drivers/byEmail")
    Call<User> getUserByEmail(@Query("email") String email);

    @PUT("/api/Drivers")
    Call<User> updateUser(@Query("id") int id, @Body User user);

    @PUT("/api/Drivers/availability")
    Call<Boolean> makeAvailable(@Query("id") int id, @Query("isAvailable") boolean isAvailable);
}
