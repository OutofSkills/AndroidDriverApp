package com.intelligentcarmanagement.carmanagementapp.utils;

public class HaversineAlgorithm {

    static final double EQUATORIAL_EARTH_RADIUS = 6378.1370D;
    static final double DISTANCE_TO_RADIANS = (Math.PI / 180D);

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dLong = (long2 - long1) * DISTANCE_TO_RADIANS;
        double dLat = (lat2 - lat1) * DISTANCE_TO_RADIANS;
        double a = Math.pow(Math.sin(dLat / 2D), 2D) + Math.cos(lat1 * DISTANCE_TO_RADIANS) * Math.cos(lat2 * DISTANCE_TO_RADIANS)
                * Math.pow(Math.sin(dLong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = EQUATORIAL_EARTH_RADIUS * c;

        return d;
    }
}
