package seng202.team1.DataAnalysis;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import javafx.beans.property.DoubleProperty;
import seng202.team1.*;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Static class that handles all of the data analysis required
 *  @author Josh Burt
 */
public final class DataAnaliser {
    private static final int RADIUS_OF_EARTH = 6371000; //Mean value for the average of earth in m
    private  static final double BASEDELTA = 0.00001;// this is roughly a meter. Very hard to get an accurate answer as the earth isn't a sphere
    /**
     * Calaculates the distance, as the crow flies between two points on a bike trip.
     * If the same bike trip is passed in twice the length of the bike trip is calculated
     * Otherwise the distance between the two starting points is calculated.
     * @param b1
     * @param b2
     * @return Double distance
     */
    public static double calculateDistOfBikeTrips(BikeTrip b1, BikeTrip b2){
        double endingLat;
        double endingLong;
        double startingLat =  Math.toRadians(Double.parseDouble(b1.getStartLatitude()));
        double startingLong = Math.toRadians(Double.parseDouble(b1.getStartLongitude()));

        if(b1 == b2){
            endingLat = Math.toRadians(Double.parseDouble(b2.getEndLatitude()));
            endingLong = Math.toRadians(Double.parseDouble(b2.getEndLongitude()));
        } else { // If the two biketrips are the same the distance is from the starting points
           endingLong = Math.toRadians(Double.parseDouble(b2.getStartLongitude()));
           endingLat = Math.toRadians(Double.parseDouble(b2.getStartLatitude()));
        }
        //the below line uses the formula of haversines to find distances using lat and long.
        double distance = 2* RADIUS_OF_EARTH * Math.asin(Math.sqrt(haversine((endingLat-startingLat))+Math.cos(startingLat)*Math.cos(endingLat)*haversine(endingLong-startingLong)));
        return distance;
    }

    /**
     * Takes a latitude, longitude and distance delta and iterates through a list of bike trips
     * returning all trips within the distance delta in an arraylist.
     * @param searchLat
     * @param searchLong
     * @param delta
     * @return
     */
    public static ArrayList<BikeTrip> searchBikeTrips(double searchLat, double searchLong, double delta){
        ArrayList<BikeTrip> trips = CSVLoader.populateBikeTrips("bikeTripTestData.csv"); // only for dev purposes needs to be changed to a database call
        trips.remove(0); // removes the heading information from index 0
        ArrayList<BikeTrip> results = new ArrayList<BikeTrip>();
        double deltaDecimals = BASEDELTA * delta; //This is the range we will search for in the dataset
        for (BikeTrip trip : trips) { //unfortunalty an 0(n) with the current data set. Perhaps we need to sort based on Lat and long to decrease time complexity
            double tripLong = Double.parseDouble(trip.getStartLongitude());
            double tripLat = Double.parseDouble(trip.getStartLatitude());
            if ((tripLong >= (searchLong - deltaDecimals)) && (tripLong <= (searchLong + deltaDecimals))){ //can safely assume all given longitudes in decimal form will be negative
                if ((tripLat >= (searchLat - deltaDecimals)) && (tripLat <= (searchLat + deltaDecimals))){ //nasty double if loop to improve readbility
                    results.add(trip);
                }
            }

        }
        return results;
    }

    /**
     * Takes a latitude, longitude and distance delta and iterates through a list of wifi points
     * Returns all points within the specified distance from the lat and long
     * @param searchLat
     * @param searchLong
     * @param delta
     * @return
     */
    public static ArrayList<WifiPoint> searchWifiPoints(double searchLat, double searchLong, double delta) {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        ArrayList<WifiPoint> results = new ArrayList<WifiPoint>();
        double deltaDecimals = BASEDELTA * delta; //This is the range we will search for in the dataset
        for (WifiPoint hotspot : hotspots) { //unfortunalty an 0(n) with the current data set. Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = Double.parseDouble(hotspot.getLongitude());
            double spotLat = Double.parseDouble(hotspot.getLatitude());
            if ((spotLong >= (searchLong - deltaDecimals)) && (spotLong <= (searchLong + deltaDecimals))){ //can safely assume all given longitudes in decimal form will be negative
                if ((spotLat >= (searchLat - deltaDecimals)) && (spotLat <= (searchLat + deltaDecimals))){ //nasty double if loop to improve readbility
                    results.add(hotspot);
                }
            }

        }
        return results;

    }
    /* locations are not provided for retaliers in GPS Coords so the current approch wont work 
    public static ArrayList<RetailerLocation> searchRetailerLocations(double searchLat, double searchLong, double delta) {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers();
        ArrayList<RetailerLocation> results  = new ArrayList<RetailerLocation>();
        double deltaDecimals = BASEDELTA * delta; //This is the range we will search for in the dataset
        for (RetailerLocation location : retailers) { //unfortunalty an 0(n) with the current data set. Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = Double.parseDouble(location.getLongitude());
            double spotLat = Double.parseDouble(location.getLatitude());
            if ((spotLong >= (searchLong - deltaDecimals)) && (spotLong <= (searchLong + deltaDecimals))){ //can safely assume all given longitudes in decimal form will be negative
                if ((spotLat >= (searchLat - deltaDecimals)) && (spotLat <= (searchLat + deltaDecimals))){ //nasty double if loop to improve readbility
                    results.add(hotspot);
                }
            }

        }
        return results;
    }*/


    /**
     * Takes an angle in radian and returns the haversine function of it.
     * Haversine is (1-Cos(theta))/2 or sin^2(theta/2)
     * This is here to clean up the math on distance calulations
     * @param theta
     * @return
     */
    private static double haversine(double theta){
        return (1-Math.cos(theta))/2;
    }
}
