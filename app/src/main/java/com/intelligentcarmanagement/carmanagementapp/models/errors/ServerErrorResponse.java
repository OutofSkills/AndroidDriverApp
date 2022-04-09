package com.intelligentcarmanagement.carmanagementapp.models.errors;

import com.google.gson.annotations.SerializedName;

public class ServerErrorResponse {
    @SerializedName("Message")
    private String message;
    @SerializedName("ErrorCode")
    private String errorCode;

    public ServerErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
