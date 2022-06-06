package com.intelligentcarmanagement.carmanagementapp.models;

public class DrivingBehaviorEvent {
    private float aggressive;
    private float normal;

    public DrivingBehaviorEvent(float aggressive, float normal) {
        this.aggressive = aggressive;
        this.normal = normal;
    }

    public void setAggressive(float aggressive) {
        this.aggressive = aggressive;
    }

    public void setNormal(float normal) {
        this.normal = normal;
    }

    public float getAggressive() {
        return aggressive;
    }

    public float getNormal() {
        return normal;
    }
}
