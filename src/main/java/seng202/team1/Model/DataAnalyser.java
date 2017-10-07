package seng202.team1.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Static class that handles all of the data analysis required.
 *
 * @author Josh Burt
 * @author Cameron Auld
 */
public final class DataAnalyser {

    // Mean value for the average radius of Earth (in metres)
    private static final int RADIUS_OF_EARTH = 6371000;

    /**
     * Calculates the distance (as the crow flies) between two points on a bike trip.
     * If the same bike trip is passed in twice, the length of the bike trip is calculated.
     * Otherwise, the distance between the two starting points is calculated.
     *
     * @param b1 first bike trip
     * @param b2 second bike trip
     * @return the distance between the two points
     */
    public static double calculateDistBetweenBikeTrips(BikeTrip b1, BikeTrip b2) {
        double startingLat = b1.getStartLatitude();
        double startingLong = b1.getStartLongitude();
        double endingLat;
        double endingLong;

        if (b1 == b2) {
            endingLat = b2.getEndLatitude();
            endingLong = b2.getEndLongitude();
        } else {
            // The two biketrips are different; calculate the distance between their starting points
            endingLat = b2.getStartLatitude();
            endingLong = b2.getStartLongitude();
        }

        // Use the formula of haversines to find distances using lat and long
        return calculateDistance(startingLat, startingLong, endingLat, endingLong);
    }


    /**
     * Takes a latitude, longitude and a distance delta and iterates through a list of bike trips
     * returning all trips within the distance delta of the point (latitude, longitude) in an arraylist.
     *
     * @param searchLat  Latitude to start the search at
     * @param searchLong Longitude to start the search at
     * @param delta      radius to search in
     * @param trips      list of bike trips
     * @param isStart    true if the start of the trip or false for the end of the trip
     * @return a list of bike trips close to the point
     */
    public static ArrayList<BikeTrip> searchBikeTrips(double searchLat, double searchLong,
                                                      double delta, ArrayList<BikeTrip> trips, boolean isStart) {
        ArrayList<BikeTrip> results = new ArrayList<BikeTrip>();

        for (BikeTrip trip : trips) {
            // unfortunately an 0(n) with the current data set.
            // Perhaps we need to sort bike trips based on lat and long to decrease time complexity
            double tripLong;
            double tripLat;
            if (isStart) {
                tripLong = trip.getStartLongitude();
                tripLat = trip.getStartLatitude();
            } else {
                tripLong = trip.getEndLongitude();
                tripLat = trip.getEndLatitude();
            }
            if (calculateDistance(searchLat, searchLong, tripLat, tripLong) < (delta + 10)) {
                results.add(trip);
            }
        }
        return results;
    }

    /**
     * returns a list of bike trips close to a point but with in a certain length
     *
     * @param searchLat  Latitude to start the search at
     * @param searchLong Longitude to start the search at
     * @param delta      radius to search in
     * @param trips      list of bike trips
     * @return a list of bike trips close to the point
     */
    public static ArrayList<BikeTrip> searchBikeTripsCamsMethod(double searchLat, double searchLong,
                                                      double delta, ArrayList<BikeTrip> trips, double min, double max) {

        ArrayList<BikeTrip> closestPoints = searchBikeTrips( searchLat, searchLong,
         delta,trips,true);
        ArrayList<BikeTrip> results = new ArrayList<>();

         for (BikeTrip point : closestPoints) {
            double tripLat = point.getStartLatitude();
            double tripLong = point.getStartLongitude();
            double distance = calculateDistance(tripLat,tripLong,searchLat,searchLong);
            if (distance > min && distance < max){
                results.add(point);
            }
         }
         return results;
    }

    /**
     * Takes a latitude, longitude and distance delta and iterates through a list of wifi points
     * Returns all points within the specified distance from the lat and long
     *
     * @param searchLat  Latitude to start the search at
     * @param searchLong Longitude to start the search at
     * @param delta      radius to search in
     * @param hotspots   ArrayList of WiFi points to search through
     * @return a list of wifi points close the point
     */
    public static ArrayList<WifiPoint> searchWifiPoints(double searchLat, double searchLong,
                                                        double delta, ArrayList<WifiPoint> hotspots) {
        ArrayList<WifiPoint> results = new ArrayList<WifiPoint>();
        for (WifiPoint hotspot : hotspots) {
            // Unfortunately an 0(n) with the current data set.
            // Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = hotspot.getLongitude();
            double spotLat = hotspot.getLatitude();
            if (calculateDistance(searchLat, searchLong, spotLat, spotLong) < delta) {
                results.add(hotspot);
            }
        }
        return results;
    }



    /**
     * Takes a latitude, longitude and distance delta and iterates through a list of wifi points
     * Returns all points within the specified distance from the lat and long
     *
     * @param searchLat  Latitude to start the search at
     * @param searchLong Longitude to start the search at
     * @param delta      radius to search in
     * @param hotspots   ArrayList of WiFi points to search through
     * @return a list of wifi points close the point
     */
    public static ObservableList<WifiPoint> searchWifiPoints(double searchLat, double searchLong,
                                                             double delta, ObservableList<WifiPoint> hotspots) {
        ArrayList<WifiPoint> results = new ArrayList<>();
        for (WifiPoint hotspot : hotspots) {
            // Unfortunately an 0(n) with the current data set.
            // Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = hotspot.getLongitude();
            double spotLat = hotspot.getLatitude();
            if (calculateDistance(searchLat, searchLong, spotLat, spotLong) < delta) {
                results.add(hotspot);
            }
        }
        ObservableList<WifiPoint> resultsObserved = FXCollections.observableArrayList(results);

        return resultsObserved;
    }

    /**
     * Takes an Arraylist of WIFIPointDistances and an Arraylist of points(a route) and returns an ArrayList of WIFIPointDistances
     * sorted by each points minimum distance to the route.
     * @param points
     * @param waypoints
     * @return
     */
    public static ArrayList<WIFIPointDistance> sortedWIFIPointsByMinimumDistanceToRoute(ArrayList<WIFIPointDistance> points, ArrayList<Point2D.Float> waypoints) {
        WIFIPointDistance current;
        Double currentDistance;
        Point2D.Float currentWayPoint;
        for (int i = 0; i < points.size(); i++)
        {
            current = points.get(i);
            for (int j = 0; j < waypoints.size(); j++) {
                currentWayPoint = waypoints.get(j);
                currentDistance = calculateDistance(current.getThisPoint().getLatitude(),current.getThisPoint().getLongitude() ,currentWayPoint.getY(),currentWayPoint.getX());
                if (currentDistance < current.getDistance()) {
                    current.setDistance(currentDistance);
                    current.setOtherPoint(new Point2D.Double(currentWayPoint.getY(), currentWayPoint.getX()));
                }
            }


        }
        points.sort(Comparator.comparing(WIFIPointDistance::getDistance));
        return points;
    }
    /**
     * Takes an Arraylist of RetailerPointDistances and an Arraylist of points(a route) and returns an ArrayList of RetailerPointDistances
     * sorted by each points minimum distance to the route.
     * @param points
     * @param waypoints
     * @return
     */
    public static ArrayList<RetailerPointDistance> sortedRetailerPointsByMinimumDistanceToRoute(ArrayList<RetailerPointDistance> points, ArrayList<Point2D.Float> waypoints) {
        RetailerPointDistance current;
        Double currentDistance;
        Point2D.Float currentWayPoint;
        for (int i = 0; i < points.size(); i++)
        {
            current = points.get(i);
            for (int j = 0; j < waypoints.size(); j++) {
                currentWayPoint = waypoints.get(j);
                currentDistance = calculateDistance(current.getThisPoint().getLatitude(),current.getThisPoint().getLongitude() ,currentWayPoint.getY(),currentWayPoint.getX());
                if (currentDistance < current.getDistance()) {
                    current.setDistance(currentDistance);
                    current.setOtherPoint(new Point2D.Double(currentWayPoint.getY(), currentWayPoint.getX()));
                }
            }


        }
        points.sort(Comparator.comparing(RetailerPointDistance::getDistance));
        return points;
    }

    /**
     *
     * @param waypoints
     * @param locations
     * @param delta
     * @return
     */
    public static ArrayList<Integer> searchRetailerLocationsOnRoute(ArrayList<Point2D.Float> waypoints, ArrayList<RetailerLocation> locations, double delta){

        Set<Integer> uniqueIndexes = new HashSet<>(locations.size());
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Integer> indexesCurrent;
        Double searchLat;
        Double searchLong;
        for (Point.Float waypoint : waypoints) {
            searchLat = waypoint.getY();
            searchLong = waypoint.getX();
            indexesCurrent = searchRetailerLocations(searchLat, searchLong, delta, locations, true);
            uniqueIndexes.addAll(indexesCurrent);

        }
        indexes.addAll(uniqueIndexes);

        return indexes;
    }


    /**
     *
     * @param waypoints
     * @param hotspots
     * @param delta
     * @return
     */
    public static ArrayList<Integer> searchWifiPointsOnRoute(ArrayList<Point2D.Float> waypoints, ArrayList<WifiPoint> hotspots, double delta){
        Set<Integer> uniqueIndexes = new HashSet<>(hotspots.size());
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Integer> indexesCurrent;
        Double searchLat;
        Double searchLong;
        for (Point.Float waypoint : waypoints) {
            searchLat = waypoint.getY();
            searchLong = waypoint.getX();
            indexesCurrent = searchWifiPoints(searchLat, searchLong, delta, hotspots, true);

            uniqueIndexes.addAll(indexesCurrent);

        }
        indexes.addAll(uniqueIndexes);

        return indexes;
    }

    /**
     *
     * @param searchLat
     * @param searchLong
     * @param delta
     * @param hotspots
     * @param returnIndex
     * @return
     */
    public static ArrayList<Integer> searchWifiPoints(double searchLat, double searchLong,
                                                        double delta, ArrayList<WifiPoint> hotspots, boolean returnIndex) {
        ArrayList<Integer> results = new ArrayList<>();
        WifiPoint point;
        for (int i = 0; i < hotspots.size(); i++) {
            point = hotspots.get(i);
            // Unfortunately an 0(n) with the current data set.
            // Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = point.getLongitude();
            double spotLat = point.getLatitude();
            if (calculateDistance(searchLat, searchLong, spotLat, spotLong) < delta) {
                results.add(i);
            }
        }

            return results;
        }

    /**
     *
     * @param searchLat
     * @param searchLong
     * @param delta
     * @param retailers
     * @param returnIndex
     * @return
     */
    public static ArrayList<Integer> searchRetailerLocations(double searchLat, double searchLong,
                                                      double delta, ArrayList<RetailerLocation> retailers, boolean returnIndex) {
        ArrayList<Integer> results = new ArrayList<>();
        RetailerLocation location;
        for (int i = 0; i < retailers.size(); i++) {
            location = retailers.get(i);
            // Unfortunately an 0(n) with the current data set.
            // Perhaps we need to sort based on Lat and long to decrease time complexity
            double spotLong = location.getLongitude();
            double spotLat = location.getLatitude();
            if (calculateDistance(searchLat, searchLong, spotLat, spotLong) < delta) {
                results.add(i);
            }
        }

        return results;
    }

    /**
     * Takes an Observable list of retailer locations and searches for all retailers within the specified distance
     *
     * @param searchLat Latitude to start searching from
     * @param searchLong longitude to start searching from
     * @param delta Distance to search within
     * @param retailers List of retailers to search through
     * @return The list of retailers within the distance from the point
     */
    public static ArrayList<RetailerLocation> searchRetailerLocations(Double searchLat, Double searchLong, Double delta, ObservableList<RetailerLocation> retailers){
        ArrayList<RetailerLocation> results = new ArrayList<>();
        for (Object shop : retailers){

            Double shopLat = ((RetailerLocation) shop).getCoords().getY();
            Double shopLong = ((RetailerLocation) shop).getCoords().getX();
            if (calculateDistance(searchLat,searchLong,shopLat,shopLong)  <= delta){
                results.add((RetailerLocation) shop);
            }
        }
        return results;
    }

    /**
     * Takes an ArrayList of retailer locations and searches for all retailers within the specified distance
     *
     * @param searchLat Latitude to start searching from
     * @param searchLong longitude to start searching from
     * @param delta Distance to search within
     * @param retailers List of retailers to search through
     * @return The list of retailers within the distance from the point
     */
    public static ArrayList<RetailerLocation> searchRetailerLocations(Double searchLat, Double searchLong, Double delta,ArrayList<RetailerLocation> retailers){
        ArrayList<RetailerLocation> results = new ArrayList<>();
        for (RetailerLocation shop : retailers){

            Double shopLat = shop.getCoords().getY();
            Double shopLong = shop.getCoords().getX();
            if (calculateDistance(searchLat,searchLong,shopLat,shopLong)  <= delta){
                results.add(shop);
            }
        }
        return results;
    }

    /**
     * Takes a Biketrip and returns the closest WifiPoint, within 1000m, to the start of the bike trip.
     * Returns null if no WifiPoint is found
     *
     * @param trip     Bike trip that needs to have the closest point found
     * @param hotspots ArrayList of WifiPoints to search through
     * @return the closest WifiPoint to the start of the bike trip
     */
    public static WifiPoint findClosestWifiToBikeRouteStart(BikeTrip trip, ArrayList<WifiPoint> hotspots) {
        double tripLat = trip.getStartLatitude();
        double tripLong = trip.getStartLongitude();
        double searchDistance = 100;
        ArrayList<WifiPoint> closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots);

        while (closeHotspots.size() == 0 && searchDistance <= 1000) {
            // add 100m to the search distance until at least one point is found or search extends
            // further than 1000m
            searchDistance += 100;

            //find all WifiPoints within given distance of given coords
            closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots);
        }

        WifiPoint closestPoint = null;
        if (closeHotspots.size() > 0) { //only search list if at least one point is found
            closestPoint = closeHotspots.get(0);
            double closestDistance = calculateDistance(tripLat, tripLong, closestPoint.getLatitude(), closestPoint.getLongitude());
            for (WifiPoint candidatePoint : closeHotspots) {
                double candidateDistance = calculateDistance(tripLat, tripLong, candidatePoint.getLatitude(), candidatePoint.getLongitude());
                if (candidateDistance < closestDistance) {
                    closestPoint = candidatePoint;
                    closestDistance = candidateDistance;
                }
            }
        }

        return closestPoint;
    }


    /**
     * Takes a BikeTrip and returns the closest WifiPoint, within 1000m, to the end of the bike trip.
     * Returns null if no WifiPoint is found.
     *
     * @param trip     Bike trip that needs to have the closest point found
     * @param hotspots ArrayList of WifiPoints to search through
     * @return the closes WifiPoint to the end of the bike trip
     */
    public static WifiPoint findClosestWifiToBikeRouteEnd(BikeTrip trip, ArrayList<WifiPoint> hotspots) {
        double tripLat = trip.getEndLatitude();
        double tripLong = trip.getEndLongitude();
        double searchDistance = 100;
        ArrayList<WifiPoint> closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots);

        while (closeHotspots.size() == 0 && searchDistance <= 1000) {
            searchDistance += 100; //add 100m to the search distance until at least one point is found or search extends further than 1000m
            closeHotspots = searchWifiPoints(tripLat, tripLong, searchDistance, hotspots); //finds all WifiPoints within given distance of given coords
        }

        WifiPoint closestPoint = null;
        if (closeHotspots.size() > 0) { //only searchList if at least one point is found
            closestPoint = closeHotspots.get(0);
            double closestDistance = calculateDistance(tripLat, tripLong, closestPoint.getLatitude(), closestPoint.getLongitude());
            for (WifiPoint candidatePoint : closeHotspots) {
                double candidateDistance = calculateDistance(tripLat, tripLong, candidatePoint.getLatitude(), candidatePoint.getLongitude());
                if (candidateDistance < closestDistance) {
                    closestPoint = candidatePoint;
                    closestDistance = candidateDistance;
                }
            }
        }

        return closestPoint;
    }


    /**
     * Takes a BikeTrip and returns the closest point to the either the start or end of the trip
     * If both closest points are the same distance apart the one closest to the start is returned.
     * If no such point exists, null is returned.
     *
     * @param trip     Bike trip that needs to have the closest point found
     * @param hotspots ArrayList of wifiPoints to search through
     * @return the closest WifiPoint to the start/end of the bike trip
     */
    public static WifiPoint findClosestWifiPointToTrip(BikeTrip trip, ArrayList<WifiPoint> hotspots) {
        double tripLat = trip.getEndLatitude();
        double tripLong = trip.getEndLongitude();
        WifiPoint closestToStart = findClosestWifiToBikeRouteStart(trip, hotspots);
        WifiPoint closestToEnd = findClosestWifiToBikeRouteEnd(trip, hotspots);

        if (closestToEnd == null) {
            return closestToStart;
        }
        if (closestToStart == null) {
            return closestToEnd;
        }

        double distanceToStart = calculateDistance(tripLat, tripLong,
                closestToStart.getLatitude(), closestToStart.getLongitude());
        double distanceToEnd = calculateDistance(tripLat, tripLong,
                closestToEnd.getLatitude(), closestToEnd.getLongitude());

        if (distanceToEnd > distanceToStart) {
            return closestToStart;
        } else {
            return closestToEnd;
        }
    }


    /**
     * Takes a list of waypoints as points and then iterates through the points and discovers
     * which is the closest wifi point on the route.
     * It then returns the Wifi point.
     *
     * @param waypoints ArrayList of waypoints
     * @param hotspots  ArrayList of wifipoints to search through
     * @return the closest wifi point to any of the waypoints
     */
    public static WifiPoint findClosestWifiToRoute(ArrayList<Point2D.Float> waypoints, ArrayList<WifiPoint> hotspots) {
        WifiPoint closestPoint = null;
        double closestDistance = 0;
        for (Point2D.Float waypoint : waypoints) {
            double pointLat = waypoint.getY();
            double pointLong = waypoint.getX();
            for (WifiPoint hotspot : hotspots) {
                double hotspotLat = hotspot.getLatitude();
                double hotspotLong = hotspot.getLongitude();
                double distance = calculateDistance(pointLat, pointLong, hotspotLat, hotspotLong);
                if (closestPoint == null) {
                    closestPoint = hotspot;
                    closestDistance = distance;
                } else if (closestDistance > distance) {
                    closestDistance = distance;
                    closestPoint = hotspot;

                }
            }
        }
        return closestPoint;
    }


    /**
     * Generic version of calculate distance using Latitude and Longitude.
     * Takes the start and end coords as arguments and then using the rule of haversines calculates
     * an approximation to the distance.
     * Given that the Earth is not a perfect sphere, this is only an approximaiton.
     * Distance is calculated as the crow flies.
     *
     * @param startLat  Double Latitude to start the search at
     * @param startLong Double Longitude to start the search at
     * @param endLat    Double Latitude to end the search at
     * @param endLong   Double Longitude to end the search at
     * @return Double
     */
    public static double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        startLat = Math.toRadians(startLat);
        startLong = Math.toRadians(startLong);
        endLat = Math.toRadians(endLat);
        endLong = Math.toRadians(endLong);

        // uses the formula of haversines to find distances using lat and long.
        return 2 * RADIUS_OF_EARTH * Math.asin(Math.sqrt(haversine(endLat - startLat) +
                Math.cos(startLat) * Math.cos(endLat) * haversine(endLong - startLong)));
    }


    /**
     * Takes a list of Biketrips and sorts it in place based on trip distance
     * Uses the default java sort and a comparator on the tripDistance.
     *
     * @param toSort ArrayList of bike trips to be sorted
     */
    public static void sortTripsByDistance(ArrayList<BikeTrip> toSort) {
        Collections.sort(toSort, new Comparator<BikeTrip>() {
            @Override
            public int compare(BikeTrip o1, BikeTrip o2) {
                return o1.getTripDistance().compareTo(o2.getTripDistance());
            }
        });

    }


    /**
     * Takes a list of waypoints and a list of retailers and returns the index of the closest retailer.
     * If no points are given, it returns -1 flag.
     *
     * @param waypoints List of waypoints to be searched
     * @param retailers List of retailers to be searched.
     * @return index of closest retailer.
     */
    public static int findClosestRetailerToBikeTrip(ArrayList<Point.Float> waypoints, ArrayList<RetailerLocation> retailers) {
        int index = -1;
        double distance = Double.POSITIVE_INFINITY;
        double testDistance;

        for (Point.Float waypoint : waypoints) {
            for (int i = 0; i < retailers.size(); i++) {
                testDistance = calculateDistance(waypoint.getY(), waypoint.getX(),
                        retailers.get(i).getLatitude(), retailers.get(i).getLongitude());
                if (testDistance < distance) {
                    distance = testDistance;
                    index = i;
                }
            }
        }

        return index;
    }


    /**
     * Takes a WifiPoint and a list of retailers and returns the index of
     * the closest Retailer to the wifipoint.
     *
     * @param hotspot   a single wifipoint
     * @param retailers an list of retailers that need the closest hotspot returned
     * @return index of closest retailer
     */
    public static int findClosestRetailerToWifiPoint(WifiPoint hotspot, ArrayList<RetailerLocation> retailers) {
        int index = -1;
        double distance = Double.POSITIVE_INFINITY;
        double testDistance;

        for (int i = 0; i < retailers.size(); i++) {
            testDistance = calculateDistance(hotspot.getLatitude(), hotspot.getLongitude(),
                    retailers.get(i).getLatitude(), retailers.get(i).getLongitude());
            if (testDistance < distance) {
                distance = testDistance;
                index = i;
            }
        }

        return index;
    }


    /**
     * Takes a Retailer and a list of WifiPoints and returns the index of
     * the closest wifiPoint to the retailer.
     *
     * @param hotspots List of Wifi hotpsots
     * @param retailer a single retailer
     * @return index of closest wifipoint
     */
    public static int findClosestWifiPointToRetailer(ArrayList<WifiPoint> hotspots, RetailerLocation retailer) {
        int index = -1;
        double distance = 1000000000;
        double testDistance;
        int i = 0;

        for (WifiPoint hotspot : hotspots) {
            testDistance = calculateDistance(hotspot.getLatitude(), hotspot.getLongitude(), retailer.getLatitude(), retailer.getLongitude());
            if (testDistance < distance) {
                distance = testDistance;
                index = i;
            }
            i++;
        }

        return index;
    }

    /**
     *
     * @param hotspots
     * @param latitude
     * @param longitude
     * @return
     */
    public static int findClosestWifiPointToRetailer(ArrayList<WifiPoint> hotspots, Float latitude, Float longitude) {
        int index = -1;
        double distance = 1000000000;
        double testDistance;
        int i = 0;

        for (WifiPoint hotspot : hotspots) {
            testDistance = calculateDistance(hotspot.getLatitude(), hotspot.getLongitude(), latitude, longitude);
            if (testDistance < distance) {
                distance = testDistance;
                index = i;
            }
            i++;
        }

        return index;
    }


    /**
     * Takes a list of wifiPoints and a point and sorts the list in place based on the distance
     * from the given point.
     * Also appends a field distanceFrom onto WifiPoint.
     *
     * @param toSort    List to be sorted
     * @param testPoint point to get Distance from
     */
    public static void sortWifiByDistanceFromPoint(ArrayList<WifiPoint> toSort, Point.Float testPoint) {
        for (WifiPoint hotspot : toSort) {
            hotspot.setDistanceFrom(calculateDistance(testPoint.getY(), testPoint.getX(),
                    hotspot.getLatitude(), hotspot.getLongitude()));
        }

        Collections.sort(toSort, new Comparator<WifiPoint>() {
            @Override
            public int compare(WifiPoint o1, WifiPoint o2) {
                return o1.getDistanceFrom().compareTo(o2.getDistanceFrom());
            }
        });

    }


    /**
     * Takes a list of RetailerLocations and sorts them in place based on the distance from the
     * test point.
     *
     * @param toSort    List of Retailers to be sorted
     * @param testPoint Point to find distance from
     */
    public static void sortRetailerByDistanceFromPoint(ArrayList<RetailerLocation> toSort, Point.Float testPoint) {
        for (RetailerLocation shop : toSort) {
            shop.setDistanceFrom(calculateDistance(testPoint.getY(), testPoint.getX(),
                    shop.getLatitude(), shop.getLongitude()));
        }

        Collections.sort(toSort, new Comparator<RetailerLocation>() {
            @Override
            public int compare(RetailerLocation o1, RetailerLocation o2) {
                return o1.getDistanceFrom().compareTo(o2.getDistanceFrom());
            }
        });

    }


    /**
     * Takes an angle in radian and returns the haversine function of it.
     * Haversine is (1-Cos(theta))/2 or sin^2(theta/2).
     * Used for distance calculations.
     *
     * @param theta Angle that needs to have its haversine found (in radians)
     * @return Haversine value
     */
    private static double haversine(double theta) {
        return (1 - Math.cos(theta)) / 2;
    }
}
