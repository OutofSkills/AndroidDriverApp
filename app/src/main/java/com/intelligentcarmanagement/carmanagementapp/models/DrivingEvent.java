package com.intelligentcarmanagement.carmanagementapp.models;

public class DrivingEvent {
    private float SuddenAcceleration;
    private float SuddenRTurn;
    private float SuddenLTurn;
    private float SuddenBreak;

    public DrivingEvent(float suddenAcceleration, float suddenRTurn, float suddenLTurn, float suddenBreak) {
        SuddenAcceleration = suddenAcceleration;
        SuddenRTurn = suddenRTurn;
        SuddenLTurn = suddenLTurn;
        SuddenBreak = suddenBreak;
    }

    public void setSuddenAcceleration(float suddenAcceleration) {
        SuddenAcceleration = suddenAcceleration;
    }

    public void setSuddenRTurn(float suddenRTurn) {
        SuddenRTurn = suddenRTurn;
    }

    public void setSuddenLTurn(float suddenLTurn) {
        SuddenLTurn = suddenLTurn;
    }

    public void setSuddenBreak(float suddenBreak) {
        SuddenBreak = suddenBreak;
    }

    public float getSuddenAcceleration() {
        return SuddenAcceleration;
    }

    public float getSuddenRTurn() {
        return SuddenRTurn;
    }

    public float getSuddenLTurn() {
        return SuddenLTurn;
    }

    public float getSuddenBreak() {
        return SuddenBreak;
    }
}
