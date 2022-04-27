package com.intelligentcarmanagement.carmanagementapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Model class for holding driver account credentials
public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("age")
    private int age;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("registrationDate")
    private String registrationDate;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("email")
    private String email;
    @SerializedName("userName")
    private String userName;
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("statusId")
    private int statusId;
    @SerializedName("deservedClients")
    private int deservedClients;
    @SerializedName("currentLat")
    private String currentLat;
    @SerializedName("currentLong")
    private String currentLong;
    @SerializedName("rating")
    private float rating;
    @SerializedName("isAvailable")
    private boolean isAvailable;

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(String currentLong) {
        this.currentLong = currentLong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getDeservedClients() {
        return deservedClients;
    }

    public void setDeservedClients(int deservedClients) {
        this.deservedClients = deservedClients;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", addressId=" + addressId +
                ", statusId=" + statusId +
                ", deservedClients=" + deservedClients +
                ", rating=" + rating +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
