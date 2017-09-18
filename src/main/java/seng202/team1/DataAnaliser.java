package seng202.team1;

import seng202.team1.BikeTrip;
import seng202.team1.CSVLoader;
import javafx.concurrent.WorkerStateEvent;
import seng202.team1.WifiPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
     * @param b1 first bike trip
     * @param b2 second bike trip
     * @return Double
     */
    public static double calculateDistOfBikeTrips(BikeTrip b1, BikeTrip b2){
        double endingLat;
        double endingLong;
        Point.Float startPoint = b1.getStartPoint();
        Point.Float endPoint;
        double startingLat =  startPoint.getX();
        double startingLong = startPoint.getY();


        if(b1 == b2){
            endPoint = b2.getEndPoint();
            endingLat = endPoint.getX();
            endingLong = endPoint.getY();
        } else { // If the two biketrips are the same the distance is from the starting points
           endPoint = b2.getStartPoint();
           endingLong = endPoint.getY();
           endingLat = endPoint.getX();
        }
        //the below line uses the formula of haversines to find distances using lat and long.
        double distance = calculateDistance(startingLat,startingLong,endingLat,endingLong);
        return distance;
    }

    /**
     * Takes a latitude, longitude and distance delta and iterates through a list of bike trips
     * returning all trips within the distance delta in an arraylist.
     * @param searchLat Latitude to start the search at
     * @param searchLong Longitude to start the search at
     * @param delta radius to search in
     * @return ArrayList<BikeTrip>
     */
    public static ArrayList<BikeTrip> searchBikeTrips(double searchLat, double searchLong, double delta, ArrayList<BikeTrip> trips){
        ArrayList<BikeTrip> results = new ArrayList<BikeTrip>();
        for (BikeTrip trip : trips) { //unfortunalty an 0(n) with the current data set. Perhaps we need to sort based on Lat and long to decrease time complexity
            Point.Float tripPoint = trip.getStartPoint();
            double tripLong = tripPoint.getY();
            double tripLat = tripPoint.getX();
            if (calculateDistance(searchLat,searchLong,tripLat,tripLong) < delta + 10) {
                results.add(trip);

            }

        }
        return results;
    }

    /**
     * Takes a latitude, longitude and distance delta and iterates through a list of wifi points
     * Returns all points within the specified distance from the lat and long
     * @param searchLat Latitude to start the search at
     * @param searchLong Longitude to start the search at
     * @param delta radius to search in
     * @param hotspots ArrayList of wifipoints to search through
     * @return ArrayList<WifiPoint>
     */
    public static ArrayList<WifiPoint> searchWifiPoints(double searchLat, double searchLong, double delta, ArrayList<WifiPoint> hotspots) {
        ArrayList<WifiPoint> results = new ArrayList<WifiPoint>();
        for (WifiPoint hotspot : hotspots) { //unfortunalty an 0(n) with the current data set. Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = Double.parseDouble(hotspot.getLongitude());
            double spotLat = Double.parseDouble(hotspot.getLatitude());
            if (calculateDistance(searchLat,searchLong,spotLat,spotLong) < delta){
                    results.add(hotspot);
                }
            }


        return results;

    }

    /**
     * Takes a Biketrip and returns the closest  WifiPoint, within 1000m, to the start of the bike trip.
     * returns null if no wifiPoint is found
     * @param trip Bike trip that needs to have the closest point found
     * @param hotspots ArrayList of wifiPoints to search through
     * @return Wifipoint
     */
    public static WifiPoint findClosestWifiToBikeRouteStart(BikeTrip trip, ArrayList<WifiPoint> hotspots) {
        double tripLat = trip.getStartPoint().getX();
        double tripLong = trip.getStartPoint().getY();
        double searchDistance = 100;
        ArrayList<WifiPoint> closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots);
        ;
        while (closeHotspots.size() == 0 && searchDistance <= 1000) {
            searchDistance += 100; //add 100m to the search distance until at least one point is found or search extends further than 1000m
            closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots); //finds all WifiPoints within given distance of given coords
        }
        WifiPoint closestPoint = null;
        if (closeHotspots.size() > 0) { //only searchList if at least one point is found
            closestPoint = closeHotspots.get(0);
            double closestDistance = calculateDistance(tripLat, tripLong, closestPoint.getLatitude(), closestPoint.getLongitude());
            for (WifiPoint canidatePoint : closeHotspots) {
                double canidateDistance = calculateDistance(tripLat, tripLong, canidatePoint.getLatitude(),canidatePoint.getLongitude());
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
     * @param trip Bike trip that needs to have the closest point found
     * @param hotspots ArrayList of wifiPoints to search through
     * @return Wifipoint
     */
    public static WifiPoint findClosestWifiToBikeRouteEnd(BikeTrip trip, ArrayList<WifiPoint> hotspots) {
        double tripLat = trip.getEndPoint().getX();
        double tripLong = trip.getEndPoint().getY();
        double searchDistance = 100;
        ArrayList<WifiPoint> closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots);
        ;
        while (closeHotspots.size() == 0 && searchDistance <= 1000) {
            searchDistance += 100; //add 100m to the search distance until at least one point is found or search extends further than 1000m
            closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots); //finds all WifiPoints within given distance of given coords
        }
        WifiPoint closestPoint = null;
        if (closeHotspots.size() > 0) { //only searchList if at least one point is found
            closestPoint = closeHotspots.get(0);
            double closestDistance = calculateDistance(tripLat, tripLong, closestPoint.getLatitude(), closestPoint.getLongitude());
            for (WifiPoint canidatePoint : closeHotspots) {
                double canidateDistance = calculateDistance(tripLat, tripLong, canidatePoint.getLatitude(), canidatePoint.getLongitude());
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
     * @param trip Bike trip that needs to have the closest point found
     * @param hotspots ArrayList of wifiPoints to search through
     * @return Wifipoint
     */
    public static WifiPoint findClosestWifiPointToTrip(BikeTrip trip, ArrayList<WifiPoint> hotspots){
        double tripLat = trip.getEndPoint().getX();
        double tripLong = trip.getEndPoint().getY();
        WifiPoint closestToStart = findClosestWifiToBikeRouteStart(trip, hotspots);
        WifiPoint closestToEnd = findClosestWifiToBikeRouteEnd(trip, hotspots);
        if (closestToEnd == null) {
            return closestToStart;
        }
        if (closestToStart == null) {
            return closestToEnd;
        }
        double distanceToStart = calculateDistance(tripLat,tripLong,
        closestToStart.getLatitude(), closestToStart.getLongitude());
        double distanceToEnd = calculateDistance(tripLat,tripLong,
        closestToEnd.getLatitude(), closestToEnd.getLongitude());
        if (distanceToEnd > distanceToStart) {
            return closestToStart;
        } else {
            return closestToEnd;
        }
    }

    /**
     * Takes a list of waypoints as points and then iterates through the points and discovers which is the closest point on the route
     * It then returns the Wifi point
     * @param waypoints ArrayList<point> of waypoint
     * @param hotspots ArrayList<WifiPoint> of wifipoints to search through
     * @return WifiPoint
     */
    public static WifiPoint findClosestWifiToRoute(ArrayList<Point2D.Float> waypoints, ArrayList<WifiPoint> hotspots){
        WifiPoint closestPoint = null;
        double closestDistance = 0;
        for (Point2D.Float waypoint : waypoints){
            double pointLat = waypoint.getX();
            double pointLong = waypoint.getY();
            for (WifiPoint hotspot : hotspots){
                double hotspotLat = Double.parseDouble(hotspot.getLatitude());
                double hotspotLong  = Double.parseDouble(hotspot.getLongitude());
                double distance = calculateDistance(pointLat,pointLong,hotspotLat,hotspotLong);
                if (closestPoint == null){
                    closestPoint = hotspot;
                    closestDistance = distance;
                } else if(closestDistance > distance){
                    closestDistance = distance;
                    closestPoint = hotspot;

                }
            }
        }
        return closestPoint;
    }


    /**
     * Generic version of calculate distance using Latitude and Longitude.
     * Takes the start and end coords as arguments and then using the rule of haversines calculates an approximation to the distance.
     * Given the non-spherical aspect of the planet it is only an approximaiton.
     * Distance is calculated as the crow flies.
     * @param startLat Double Latitude to start the search at
     * @param startLong Double Longitude to start the search at
     * @param endLat Double Latitude to end the search at
     * @param endLong  Double Longitude to end the search at
     * @return Double
     */
    public static double calculateDistance(double startLat, double startLong, double endLat, double endLong){
        startLat = Math.toRadians(startLat);
        startLong = Math.toRadians(startLong);
        endLat = Math.toRadians(endLat);
        endLong = Math.toRadians(endLong);
        //the below line uses the formula of haversines to find distances using lat and long.
        double distance = 2* RADIUS_OF_EARTH * Math.asin(Math.sqrt(haversine((endLat-startLat))+Math.cos(startLat)*Math.cos(endLat)*haversine(endLong-startLong)));
        return distance;
    }


    /**
     * Takes a list of Biketrips and sorts it in place based on trip distance
     * Uses the default java sort and a comparator on the tripDistance
     * @param toSort ArrayList of bike trips to be sorted
     */
    public static void sortTripsByDistance(ArrayList<BikeTrip> toSort){
        Collections.sort(toSort, new Comparator<BikeTrip>() {
            @Override
            public int compare(BikeTrip o1, BikeTrip o2) {
                return o1.getTripDistance().compareTo(o2.getTripDistance());
            }
        });

    }

    /**
     * Takes an angle in radian and returns the haversine function of it.
     * Haversine is (1-Cos(theta))/2 or sin^2(theta/2)
     * This is here to clean up the math on distance calulations
     * @param theta Angle that needs to have its haversine found must be in radians
     * @return Double Haversine value
     */
    private static double haversine(double theta){
        return (1-Math.cos(theta))/2;
    }
}
