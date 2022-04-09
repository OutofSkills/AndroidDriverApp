package com.intelligentcarmanagement.carmanagementapp.models.account;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordDTO {
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("currentPassword")
    private String currentPassword;
    @SerializedName("password")
    private String newPassword;
    @SerializedName("confirmPassword")
    private String confirmNewPassword;

    public ChangePasswordDTO(int id, String email, String currentPassword, String newPassword, String confirmNewPassword) {
        this.id = id;
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
