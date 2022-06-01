package com.intelligentcarmanagement.carmanagementapp.models.ride;

import com.intelligentcarmanagement.carmanagementapp.models.Client;
import com.intelligentcarmanagement.carmanagementapp.utils.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Ride implements Serializable {
    private int id;
    private int driverId;
    private int clientId;
    private String pickUpPlaceAddress;
    private String pickUpPlaceName;
    private String destinationPlaceAddress;
    private String destinationPlaceName;
    private String pickUpPlaceLat;
    private String pickUpPlaceLong;
    private String destinationPlaceLat;
    private String destinationPlaceLong;
    private double distance;
    private double averageTime;
    private double price;
    private String pickUpTime;
    private Client client;
    private RideState rideState;
    private Review review;

    public Ride() {
    }

    public Ride(int id, int driverId, int clientId, String pickUpPlaceAddress, String pickUpPlaceName,
                String destinationPlaceAddress, String destinationPlaceName, String pickUpPlaceLat, String pickUpPlaceLong,
                String destinationPlaceLat, String destinationPlaceLong, double distance, double averageTime, double price,
                String pickUpTime, Client client, RideState rideState, Review review) {
        this.id = id;
        this.driverId = driverId;
        this.clientId = clientId;
        this.pickUpPlaceAddress = pickUpPlaceAddress;
        this.pickUpPlaceName = pickUpPlaceName;
        this.destinationPlaceAddress = destinationPlaceAddress;
        this.destinationPlaceName = destinationPlaceName;
        this.pickUpPlaceLat = pickUpPlaceLat;
        this.pickUpPlaceLong = pickUpPlaceLong;
        this.destinationPlaceLat = destinationPlaceLat;
        this.destinationPlaceLong = destinationPlaceLong;
        this.distance = distance;
        this.averageTime = averageTime;
        this.price = price;
        this.pickUpTime = pickUpTime;
        this.client = client;
        this.rideState = rideState;
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RideState getRideState() {
        return rideState;
    }

    public void setRideState(RideState rideState) {
        this.rideState = rideState;
    }

    public String getPickUpPlaceLat() {
        return pickUpPlaceLat;
    }

    public void setPickUpPlaceLat(String pickUpPlaceLat) {
        this.pickUpPlaceLat = pickUpPlaceLat;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getAverageTime() {
        double averageSpeed = 40 * Constants.KM_HOUR_TO_MINUTES; //40 KM/H to minutes

        return distance / averageSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPickUpPlaceName() {
        return pickUpPlaceName;
    }

    public void setPickUpPlaceName(String pickUpPlaceName) {
        this.pickUpPlaceName = pickUpPlaceName;
    }

    public String getDestinationPlaceName() {
        return destinationPlaceName;
    }

    public void setDestinationPlaceName(String destinationPlaceName) {
        this.destinationPlaceName = destinationPlaceName;
    }

    public Date getPickUpTime() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH).parse(pickUpTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getPickUpPlaceAddress() {
        return pickUpPlaceAddress;
    }

    public void setPickUpPlaceAddress(String pickUpPlaceAddress) {
        this.pickUpPlaceAddress = pickUpPlaceAddress;
    }

    public String getDestinationPlaceAddress() {
        return destinationPlaceAddress;
    }

    public void setDestinationPlaceAddress(String destinationPlaceAddress) {
        this.destinationPlaceAddress = destinationPlaceAddress;
    }

    public String getPickUpLat() {
        return pickUpPlaceLat;
    }

    public void setPickUpLat(String pickUpLat) {
        this.pickUpPlaceLat = pickUpLat;
    }

    public String getPickUpPlaceLong() {
        return pickUpPlaceLong;
    }

    public void setPickUpPlaceLong(String pickUpPlaceLong) {
        this.pickUpPlaceLong = pickUpPlaceLong;
    }

    public String getDestinationPlaceLat() {
        return destinationPlaceLat;
    }

    public void setDestinationPlaceLat(String destinationPlaceLat) {
        this.destinationPlaceLat = destinationPlaceLat;
    }

    public String getDestinationPlaceLong() {
        return destinationPlaceLong;
    }

    public void setDestinationPlaceLong(String destinationPlaceLong) {
        this.destinationPlaceLong = destinationPlaceLong;
    }
}
