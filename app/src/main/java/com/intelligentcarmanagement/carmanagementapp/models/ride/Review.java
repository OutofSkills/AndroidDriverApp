package com.intelligentcarmanagement.carmanagementapp.models.ride;

public class Review {
    private int id;
    private double rating;
    private double drivingAccuracy;

    public Review(int id, double rating, double drivingAccuracy) {
        this.id = id;
        this.rating = rating;
        this.drivingAccuracy = drivingAccuracy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getDrivingAccuracy() {
        return drivingAccuracy;
    }

    public void setDrivingAccuracy(double drivingAccuracy) {
        this.drivingAccuracy = drivingAccuracy;
    }
}
