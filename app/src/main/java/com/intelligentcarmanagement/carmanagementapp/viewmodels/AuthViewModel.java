package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.utils.JwtParser;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthViewModel extends AndroidViewModel {
    private MutableLiveData<String> mEmailMutableData = new MutableLiveData<>();

    private DatabaseHelper dbHelper;
    private Map<Object, Object> claims;

    public AuthViewModel(Application context) {
        super(context);
        dbHelper = new DatabaseHelper(context);
    }

    // Return true if the user token is valid
    // and false if it is null or expired
    public boolean IsAuthenticated() {
        String token = dbHelper.GetToken();
        if (token == null)
            return false;

        try {
            String decodedPayload = JwtParser.decoded(token);
            claims = getClaims(decodedPayload);

            // If the token is expired
            // remove it and redirect the user to the login screen
            if(isTokenExpired(Long.parseLong(String.valueOf(claims.get("exp"))))) {
                Log.d("AuthViewModel", "Token expired");
                dbHelper.RemoveToken();
                return false;
            }
            mEmailMutableData.postValue(String.valueOf(claims.get("email")));

            return true;
        } catch (Exception e) {
            Log.d("AuthViewModel", "Claims decoding error: " + e.getMessage());
        }

        // Shouldn't reach here
        return false;
    }

    // Convert a json string to a Map
    private Map<Object, Object> getClaims(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Map.class);
    }

    // Verify if the token expiration date in seconds is
    // bigger than the current time in milliseconds
    private boolean isTokenExpired(long timeSpan) {
        return System.currentTimeMillis()/1000 >= timeSpan ? true : false;
    }

    // Return the email if the user is authenticated
    public LiveData<String> getLoginResult()
    {
        return mEmailMutableData;
    }
}