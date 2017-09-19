package seng202.team1;


;
import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import org.junit.Test;
import org.junit.runner.notification.RunListener;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class DataAnaliserTest{


    @Test
    public void TestDistanceBikeTripsDifferant(){
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip trip1 = testData.get(1);
        BikeTrip trip2 = testData.get(2);
        double distance = DataAnaliser.calculateDistOfBikeTrips(trip1,trip2);
        assertEquals(1190, distance , 10.0);

    }

    @Test
    public void TestDistanceBikeTripsSame(){
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip trip1 = testData.get(1);
        BikeTrip trip2 = testData.get(1);
        double distance = DataAnaliser.calculateDistOfBikeTrips(trip1,trip2);
        assertEquals(922, distance , 10.0);

    }

    @Test
    public void TestSearchBikeTripsMatching(){ //Needs better data to be checked with #TODO
        ArrayList<BikeTrip> trips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<BikeTrip> results = DataAnaliser.searchBikeTrips(40.732,-73.9925,600, trips); // should be in the middle of the test data
        assertEquals(2, results.size());
    }

    @Test
    public void TestSearchWifiPoints(){ //Needs better data to be checked with #TODO
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        ArrayList<WifiPoint> results = DataAnaliser.searchWifiPoints(40.755, -73.985,600,hotspots);
        assertEquals(3, results.size());
    }

    @Test
    public void TestCalculateDistance(){
        double distance = DataAnaliser.calculateDistance(40.767, -73.933,40.77,-73.94);
        assertEquals(680, distance, 3);
    }

    @Test
    public void TestSortTripsByDistance() {
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<BikeTrip> oldData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        Collections.shuffle(testData, new Random(2132154541));
        Collections.shuffle(oldData, new Random(2132154541));
        DataAnaliser.sortTripsByDistance(testData);// sorts in place
        assertEquals(testData.get(0), oldData.get(0));
        assertEquals(testData.get(1), oldData.get(3));
        assertEquals(testData.get(2), oldData.get(2));
        assertEquals(testData.get(3), oldData.get(1));
    }

    @Test
    public void TestFindClosestWifiPointToStartPointExists(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(3);
        WifiPoint testPoint = DataAnaliser.findClosestWifiToBikeRouteStart(testTrip, hotspots);
        assertEquals(hotspots.get(1), testPoint); // can't test just yet as need the point class the Ollie is working on
    }
    @Test
    public void TestFindClosestWifiPointToStartPointDoesNotExists(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(0);
        WifiPoint testPoint = DataAnaliser.findClosestWifiToBikeRouteStart(testTrip, hotspots);
        assertEquals(null, testPoint); // can't test just yet as need the point class the Ollie is working on
    }

    @Test
    public void TestFIndClosestWifiPointToEndExists(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(3);
        WifiPoint testPoint = DataAnaliser.findClosestWifiToBikeRouteEnd(testTrip, hotspots);
        assertEquals(hotspots.get(2),testPoint);
    }

    @Test
    public void TestFindClosestWifiPointToEndDoesNotExist(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(0);
        WifiPoint testPoint = DataAnaliser.findClosestWifiToBikeRouteEnd(testTrip, hotspots);
        assertEquals(null,testPoint);
    }

    @Test
    public void TestFindClosestPointToTripExists() {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(3);
        WifiPoint testPoint = DataAnaliser.findClosestWifiPointToTrip(testTrip,hotspots);
        assertEquals(hotspots.get(2), testPoint);
    }

    @Test
    public void TestFindClosestPointToTripDoesNotExists() {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(1);
        WifiPoint testPoint = DataAnaliser.findClosestWifiPointToTrip(testTrip,hotspots);
        assertEquals(null, testPoint);
    }

    @Test
    public void TestFindClosestWifiPointToRoute(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        ArrayList<Point2D.Float> waypoints = new ArrayList<Point2D.Float>();
        for(BikeTrip trip : bikeTrips){
            Point2D.Float point = trip.getStartPoint();
             waypoints.add(point);
        }
        WifiPoint closestPoint = DataAnaliser.findClosestWifiToRoute(waypoints,hotspots);
        assertEquals(hotspots.get(1),closestPoint);
    }


}
