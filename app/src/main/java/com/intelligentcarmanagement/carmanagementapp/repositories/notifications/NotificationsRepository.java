package com.intelligentcarmanagement.carmanagementapp.repositories.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.intelligentcarmanagement.carmanagementapp.api.RetrofitService;
import com.intelligentcarmanagement.carmanagementapp.api.notifications.INotificationsRequests;
import com.intelligentcarmanagement.carmanagementapp.api.notifications.responses.IGetNotifications;
import com.intelligentcarmanagement.carmanagementapp.api.notifications.responses.IUpdateToken;
import com.intelligentcarmanagement.carmanagementapp.models.notifications.Notification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsRepository implements INotificationsRepository {

    @Override
    public void updateToken(String token, String id, String firebaseToken, IUpdateToken updateTokenResponse) {
        INotificationsRequests notificationsRequests = RetrofitService.getRetrofit().create(INotificationsRequests.class);
        Call<String> initRequest = notificationsRequests.updateToken(token, id, firebaseToken);

        initRequest.enqueue(new Callback<String>() {

            @Override
            public void onResponse(@NonNull Call<String> call, Response<String> response) {
                Log.d("Repo", "Body: " + response.body());
                if(response.isSuccessful()) {
                    Log.d("Repo", "Body: " + response.body());
                    updateTokenResponse.onResponse(response.body());
                }
                else
                {
                    updateTokenResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Repo", "Exception: " + t.getMessage());
                updateTokenResponse.onFailure(t);
            }
        });
    }

    @Override
    public void getNotifications(String token, String id, IGetNotifications getNotifications) {
        INotificationsRequests notificationsRequests = RetrofitService.getRetrofit().create(INotificationsRequests.class);
        Call<ArrayList<Notification>> initRequest = notificationsRequests.getNotifications(token, id);

        initRequest.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                Log.d("Repo", "Body: " + response.body());
                getNotifications.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                getNotifications.onFailure(t);
            }
        });
    }
}
