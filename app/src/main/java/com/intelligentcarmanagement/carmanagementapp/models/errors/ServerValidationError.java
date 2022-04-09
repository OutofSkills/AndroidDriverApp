package com.intelligentcarmanagement.carmanagementapp.models.errors;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ServerValidationError {
    private Map<String, String[]> errors;
    private String type;
    private String title;
    private String status;
    private String traceId;

    public ServerValidationError(Map<String, String[]> errors, String type, String title, String status, String traceId) {
        this.errors = errors;
        this.type = type;
        this.title = title;
        this.status = status;
        this.traceId = traceId;
    }

    public Map<String, String[]> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String[]> errors) {
        this.errors = errors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "ServerValidationError{" +
                "errors=" + errors +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
