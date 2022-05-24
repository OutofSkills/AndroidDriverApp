package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.intelligentcarmanagement.carmanagementapp.services.TokenService;
import com.intelligentcarmanagement.carmanagementapp.services.SessionManager;

import java.util.Map;

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
        String token = sessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);
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