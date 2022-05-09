package com.intelligentcarmanagement.carmanagementapp.models.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("jwtToken")
    private String jwtToken;
    @SerializedName("firebaseToken")
    private String firebaseToken;

    public LoginResponse(String jwtToken, String firebaseToken) {
        this.jwtToken = jwtToken;
        this.firebaseToken = firebaseToken;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
