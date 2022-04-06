package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.services.TokenService;
import com.intelligentcarmanagement.carmanagementapp.utils.JwtParser;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthViewModel extends AndroidViewModel {
    private SessionManager sessionManager;
    private Map<Object, Object> claims;

    public AuthViewModel(Application context) {
        super(context);
        sessionManager = new SessionManager(context);
    }

    // Return true if the user token is valid
    // and false if it is null or expired
    public boolean IsAuthenticated() {
        String token = sessionManager.getUserData().get(sessionManager.KEY_TOKEN);
        if (token == null)
            return false;

        try {
            claims = new TokenService().decodePayloadClaims(token);
            // If the token is expired
            // remove it and redirect the user to the login screen
            if(TokenService.isTokenExpired(Long.parseLong(String.valueOf(claims.get("exp"))))) {
                Log.d("AuthViewModel", "Token expired");
                sessionManager.clearSession();
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.d("AuthViewModel", "Claims decoding error: " + e.getMessage());
        }

        // Shouldn't reach here
        return false;
    }
}