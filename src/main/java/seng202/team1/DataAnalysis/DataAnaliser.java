package seng202.team1.DataAnalysis;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import javafx.beans.property.DoubleProperty;
import seng202.team1.*;

import javax.xml.transform.dom.DOMLocator;
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
        double distance = calculateDistance(startingLat,startingLong,endingLat,endingLong);
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
     * Takes a Biketrip and returns the closest  WifiPoint, within 1000m, to the start of the bike trip.
     * returns null if no wifiPoint is found
     * @param trip
     * @return
     */
    public static WifiPoint findClosestWifiToBikeRouteStart(BikeTrip trip) {
        double tripLong = Double.parseDouble(trip.getStartLongitude());
        double tripLat = Double.parseDouble(trip.getStartLatitude());
        double searchDistance = 100;
        ArrayList<WifiPoint> closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance);
        ;
        while (closeHotspots.size() == 0 && searchDistance <= 1000) {
            searchDistance += 100; //add 100m to the search distance until at least one point is found or search extends further than 1000m
            closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance); //finds all WifiPoints within given distance of given coords
        }
        WifiPoint closestPoint = null;
        if (closeHotspots.size() > 0) { //only searchList if at least one point is found
            closestPoint = closeHotspots.get(0);
            double closestDistance = calculateDistance(tripLat, tripLong, Double.parseDouble(closestPoint.getLatitude()), Double.parseDouble(closestPoint.getLongitude()));
            for (WifiPoint canidatePoint : closeHotspots) {
                double canidateDistance = calculateDistance(tripLat, tripLong, Double.parseDouble(canidatePoint.getLatitude()), Double.parseDouble(canidatePoint.getLongitude()));
                if (canidateDistance < closestDistance) {
                    closestPoint = canidatePoint;
                    closestDistance = canidateDistance;
                }
            }
        }
        return closestPoint;
    }
    /**
     * Takes a Biketrip and returns the closest  WifiPoint, within 1000m, to the end of the bike trip.
     * returns null if no wifiPoint is found
     * @param trip
     * @return
     */
    public static WifiPoint findClosestWifiToBikeRouteEnd(BikeTrip trip) {
        double tripLong = Double.parseDouble(trip.getEndLongitude());
        double tripLat = Double.parseDouble(trip.getEndLatitude());
        double searchDistance = 100;
        ArrayList<WifiPoint> closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance);
        ;
        while (closeHotspots.size() == 0 && searchDistance <= 1000) {
            searchDistance += 100; //add 100m to the search distance until at least one point is found or search extends further than 1000m
            closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance); //finds all WifiPoints within given distance of given coords
        }
        WifiPoint closestPoint = null;
        if (closeHotspots.size() > 0) { //only searchList if at least one point is found
            closestPoint = closeHotspots.get(0);
            double closestDistance = calculateDistance(tripLat, tripLong, Double.parseDouble(closestPoint.getLatitude()), Double.parseDouble(closestPoint.getLongitude()));
            for (WifiPoint canidatePoint : closeHotspots) {
                double canidateDistance = calculateDistance(tripLat, tripLong, Double.parseDouble(canidatePoint.getLatitude()), Double.parseDouble(canidatePoint.getLongitude()));
                if (canidateDistance < closestDistance) {
                    closestPoint = canidatePoint;
                    closestDistance = canidateDistance;
                }
            }
        }
        return closestPoint;
    }

    /**
     * Takes a BikeTrip and returns the closest point to the either the start or end of the trip
     * If both closest points are the same distance apart the one closest to the start is returned.
     * If no such point exists null is returned
     * @param trip
     * @return
     */
    public static WifiPoint findClosestWifiPointToTrip(BikeTrip trip){
        double tripLong = Double.parseDouble(trip.getEndLongitude());
        double tripLat = Double.parseDouble(trip.getEndLatitude());
        WifiPoint closestToStart = findClosestWifiToBikeRouteStart(trip);
        WifiPoint closestToEnd = findClosestWifiToBikeRouteEnd(trip);
        if (closestToEnd == null) {
            return closestToStart;
        }
        if (closestToStart == null) {
            return closestToEnd;
        }
        double distanceToStart = calculateDistance(tripLat,tripLong,Double.parseDouble(closestToStart.getLatitude()),Double.parseDouble(closestToStart.getLongitude()));
        double distanceToEnd = calculateDistance(tripLat,tripLong,Double.parseDouble(closestToEnd.getLatitude()), Double.parseDouble(closestToEnd.getLongitude()));
        if (distanceToEnd > distanceToStart) {
            return closestToStart;
        } else {
            return closestToEnd;
        }
    }
    

    /**
     * Generic version of calculate distance using Latitude and Longitude.
     * Takes the start and end coords as arguments and then using the rule of haversines calculates an approximation to the distance.
     * Given the non-spherical aspect of the planet it is only an approximaiton.
     * Distance is calculated as the crow flies.
     * @param startLat
     * @param startLong
     * @param endLat
     * @param endLong
     * @return
     */
    public static double calculateDistance(double startLat, double startLong, double endLat, double endLong){

        //the below line uses the formula of haversines to find distances using lat and long.
        double distance = 2* RADIUS_OF_EARTH * Math.asin(Math.sqrt(haversine((endLat-startLat))+Math.cos(startLat)*Math.cos(endLat)*haversine(endLong-startLong)));
        return distance;
    }

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
