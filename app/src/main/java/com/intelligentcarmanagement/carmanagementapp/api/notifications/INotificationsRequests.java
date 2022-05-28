package com.intelligentcarmanagement.carmanagementapp.api.notifications;

import com.intelligentcarmanagement.carmanagementapp.models.Notification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface INotificationsRequests {
    @PUT("/api/Notifications/token")
    @Headers("Accept: application/json")
    Call<String> updateToken(@Header("authorization") String token, @Query("id") String id, @Body String firebaseToken);

    @GET("/api/Notifications")
    Call<ArrayList<Notification>> getNotifications(@Header("authorization") String token, @Query("userId") String id);
}
