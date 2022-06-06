package com.intelligentcarmanagement.carmanagementapp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.utils.JwtParser;

import java.util.Map;

public class TokenService {
    public Map<Object, Object> decodePayloadClaims(String token)
    {
        Map<Object, Object> claims = null;
        try {
            String decodedPayload = JwtParser.decoded(token);
            claims = getClaims(decodedPayload);
        }catch (Exception e)
        {
            Log.d("TokenService", "Payload decode error: " + e.getMessage());
        }
        return claims;
    }

    // Convert a json string to a Map
    private Map<Object, Object> getClaims(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Map.class);
    }

    // Verify if the token expiration date in seconds is
    // bigger than the current time in milliseconds
    public static boolean isTokenExpired(long timeSpan) {
        return System.currentTimeMillis()/1000 >= timeSpan ? true : false;
    }

}

