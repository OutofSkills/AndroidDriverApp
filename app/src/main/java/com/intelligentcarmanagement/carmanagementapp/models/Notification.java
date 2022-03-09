package com.intelligentcarmanagement.carmanagementapp.models;

import android.graphics.Bitmap;

import java.util.Date;

public class Notification {
    private String name;
    private String description;
    private Date date;
    private Bitmap icon;

    public Notification(String name, String description, Date date, Bitmap icon) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
