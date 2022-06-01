package com.intelligentcarmanagement.carmanagementapp.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Model class for holding driver account credentials
public class Client implements Serializable {
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
    @SerializedName("rating")
    private double rating;
    @SerializedName("addressId")
    private int addressId;
    @SerializedName("statusId")
    private int statusId;

    public Client(int id, String firstName, String lastName, int age, String phoneNumber, String registrationDate, String avatar, String email, String userName, double rating, int addressId, int statusId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.avatar = avatar;
        this.email = email;
        this.userName = userName;
        this.rating = rating;
        this.addressId = addressId;
        this.statusId = statusId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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
                '}';
    }
}