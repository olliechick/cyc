package seng202.team1.DataAnalysis;

import seng202.team1.BikeTrip;
import seng202.team1.WifiPoint;

/**
 * Static class that handles all of the data analysis required
 *  @author Josh Burt
 */
public final class DataAnaliser {
    private static final int RADIUS_OF_EARTH = 6371000; //Mean value for the average of earth in m
    /**
     * Calaculates the distance, as the crow flies between two points on a bike trip.
     * If the same bike trip is passed in twice the length of the bike trip is calculated
     * Otherwise the distance between the two starting points is calculated.
     * @param b1
     * @param b2
     * @return Double distance
     */
    public static double calculateDistFromLatandLong(BikeTrip b1, BikeTrip b2){
        double endingLat;
        double endingLong;
        double startingLat =  Math.toRadians(Double.parseDouble(b1.getStartLatitude()));
        double startingLong = Math.toRadians(Double.parseDouble(b1.getStartLongitude()));

        if(b1 == b2){
            endingLat = Math.toRadians(Double.parseDouble(b2.getEndLatitude()));
            endingLong = Math.toRadians(Double.parseDouble(b2.getEndLongitude()));
        } else { // If the two biketrips are the saem the distance is from the starting points
           endingLong = Math.toRadians(Double.parseDouble(b2.getStartLongitude()));
           endingLat = Math.toRadians(Double.parseDouble(b2.getStartLatitude()));
        }
        //the below line uses the formula of haversines to find distances using lat and long.
        double distance = 2* RADIUS_OF_EARTH * Math.asin(Math.sqrt(haversine((endingLat-startingLat))+Math.cos(startingLat)*Math.cos(endingLat)*haversine(endingLong-startingLong)));
        return distance;
    }

    /**
     * Calculates the distance as the crow flies between two reatilers
     * @param w1
     * @param w2
     * @return double distance
     */
    public static double calculateDistFromLatandLong(WifiPoint w1, WifiPoint w2){
        double startingLat = Math.toRadians(Double.parseDouble(w1.getLatitude()));
        double startingLong = Math.toRadians(Double.parseDouble(w1.getLongitude()));
        double endingLat = Math.toRadians(Double.parseDouble(w2.getLatitude()));
        double endingLong = Math.toRadians(Double.parseDouble(w2.getLongitude()));

        //the below line uses the formula of haversines to find distances using lat and long.
        double distance = 2* RADIUS_OF_EARTH * Math.asin(Math.sqrt(haversine((endingLat-startingLat))+Math.cos(startingLat)*Math.cos(endingLat)*haversine(endingLong-startingLong)));
        return distance;
    }

    /**
     * Takes an angle in radian and returns the haversine function of it.
     * Haversine is (1-Cos(theta))/2 or sin^2(theta/2)
     * @param theta
     * @return
     */
    private static double haversine(double theta){
        return (1-Math.cos(theta))/2;
    }
}
