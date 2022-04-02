package com.intelligentcarmanagement.carmanagementapp.models.Login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("grant_Type")
    private String grant;


    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest(String email, String password, String grant) {
        this.email = email;
        this.password = password;
        this.grant = grant;
    }

    public String getGrant() {
        return grant;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
