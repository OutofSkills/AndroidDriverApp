package com.intelligentcarmanagement.carmanagementapp.models;

import java.util.Date;

public class Ride {
    private int Id;
    private String pickUpAddress;
    private String destinationAddress;
    private Date RideDate;
    private double distance;

    public Ride(int id, String pickUpAddress, String destinationAddress, Date rideDate, double distance) {
        Id = id;
        this.pickUpAddress = pickUpAddress;
        this.destinationAddress = destinationAddress;
        RideDate = rideDate;
        this.distance = distance;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Date getRideDate() {
        return RideDate;
    }

    public void setRideDate(Date rideDate) {
        RideDate = rideDate;
    }
}
