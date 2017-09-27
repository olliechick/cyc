package seng202.team1;


import org.junit.Test;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.DataAnalyser;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class DataAnalyserTest {


    @Test
    public void TestDistanceBikeTripsDifferent() throws Exception {
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip trip1 = testData.get(1);
        BikeTrip trip2 = testData.get(2);
        double distance = DataAnalyser.calculateDistBetweenBikeTrips(trip1,trip2);
        assertEquals(1190, distance , 10.0);

    }

    @Test
    public void TestDistanceBikeTripsSame() throws Exception {
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip trip1 = testData.get(1);
        BikeTrip trip2 = testData.get(1);
        double distance = DataAnalyser.calculateDistBetweenBikeTrips(trip1,trip2);
        assertEquals(922, distance , 10.0);

    }

    @Test
    public void TestSearchBikeTripsMatching() throws Exception { //Needs better data to be checked with #TODO
        ArrayList<BikeTrip> trips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<BikeTrip> results = DataAnalyser.searchBikeTrips(40.732,-73.9925,600, trips); // should be in the middle of the test data
        assertEquals(2, results.size());
    }

    @Test
    public void TestSearchWifiPoints() throws Exception { //Needs better data to be checked with #TODO
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        ArrayList<WifiPoint> results = DataAnalyser.searchWifiPoints(40.755, -73.985,600,hotspots);
        assertEquals(3, results.size());
    }

    @Test
    public void TestCalculateDistance() throws Exception {
        double distance = DataAnalyser.calculateDistance(40.767, -73.933,40.77,-73.94);
        assertEquals(680, distance, 3);
    }

    @Test
    public void TestSortTripsByDistance() throws Exception {
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<BikeTrip> oldData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        Collections.shuffle(testData, new Random(2132154541));
        Collections.shuffle(oldData, new Random(2132154541));
        System.out.println(testData.size()+""+ testData); //TODO debug print statement
        DataAnalyser.sortTripsByDistance(testData); // sorts in place
        assertEquals(testData.get(0), oldData.get(0));
        assertEquals(testData.get(1), oldData.get(3));
        assertEquals(testData.get(2), oldData.get(2));
        assertEquals(testData.get(3), oldData.get(1));
    }

    @Test
    public void TestFindClosestWifiPointToStartPointExists() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(3);
        WifiPoint testPoint = DataAnalyser.findClosestWifiToBikeRouteStart(testTrip, hotspots);
        assertEquals(hotspots.get(1), testPoint); // can't test just yet as need the point class the Ollie is working on
    }
    @Test
    public void TestFindClosestWifiPointToStartPointDoesNotExists() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(0);
        WifiPoint testPoint = DataAnalyser.findClosestWifiToBikeRouteStart(testTrip, hotspots);
        assertEquals(null, testPoint); // can't test just yet as need the point class the Ollie is working on
    }

    @Test
    public void TestFIndClosestWifiPointToEndExists() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(3);
        WifiPoint testPoint = DataAnalyser.findClosestWifiToBikeRouteEnd(testTrip, hotspots);
        assertEquals(hotspots.get(2),testPoint);
    }

    @Test
    public void TestFindClosestWifiPointToEndDoesNotExist() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(0);
        WifiPoint testPoint = DataAnalyser.findClosestWifiToBikeRouteEnd(testTrip, hotspots);
        assertEquals(null,testPoint);
    }

    @Test
    public void TestFindClosestPointToTripExists() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(3);
        WifiPoint testPoint = DataAnalyser.findClosestWifiPointToTrip(testTrip,hotspots);
        assertEquals(hotspots.get(2), testPoint);
    }

    @Test
    public void TestFindClosestPointToTripDoesNotExists() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        BikeTrip testTrip = bikeTrips.get(1);
        WifiPoint testPoint = DataAnalyser.findClosestWifiPointToTrip(testTrip,hotspots);
        assertEquals(null, testPoint);
    }

    @Test
    public void TestFindClosestWifiPointToRoute() throws Exception {
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("wifiTester.csv");
        ArrayList<Point2D.Float> waypoints = new ArrayList<Point2D.Float>();
        for(BikeTrip trip : bikeTrips){
            Point2D.Float point = trip.getStartPoint();
             waypoints.add(point);
        }
        WifiPoint closestPoint = DataAnalyser.findClosestWifiToRoute(waypoints,hotspots);
        assertEquals(hotspots.get(1),closestPoint);
    }

    @Test
    public void TestFindClosestRetailerToBikeTrip() throws Exception{
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        ArrayList<Point.Float> waypoints = new ArrayList<>();
        for(BikeTrip trip : bikeTrips){
            waypoints.add(trip.getStartPoint());
            waypoints.add(trip.getEndPoint());
        }
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers("src/test/resources/Lower_Manhattan_Retailers.csv");
        int result = DataAnalyser.findClosestRetailerToBikeTrip(waypoints,retailers);
        assertEquals(90, result);

    }

    @Test
    public void TestFindClosesRetailerToWifi() throws Exception{
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("src/test/resources/testWifi.csv");
        WifiPoint hotspot = hotspots.get(0);
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers("src/test/resources/Lower_Manhattan_Retailers.csv");
        int result = DataAnalyser.findClosestRetailerToWifiPoint(hotspot,retailers);
        assertEquals(543, result);

    }
    @Test
    public void TestFindClosestWifiToRetailer() throws Exception{
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers("src/test/resources/Lower_Manhattan_Retailers.csv");
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("src/test/resources/testWifi.csv");
        RetailerLocation retailer = retailers.get(0);
        int result = DataAnalyser.findClosestWifiPointToRetailer(hotspots,retailer);
        assertEquals(2,result);
    }

    @Test
    public void TestSortWifiByDistanceFromPoint() throws Exception{
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots("src/test/resources/testWifi.csv");
        ArrayList<WifiPoint> originalArray = CSVLoader.populateWifiHotspots("src/test/resources/testWifi.csv");
        Point.Float testPoint = new Point2D.Float((float)-73.96753849952732,(float)40.76045675959568);
        DataAnalyser.sortWifiByDistanceFromPoint(hotspots,testPoint);
        assertEquals(hotspots.get(0), originalArray.get(4));
        assertEquals(hotspots.get(1), originalArray.get(3));
        assertEquals(hotspots.get(2), originalArray.get(2));
        assertEquals(hotspots.get(3), originalArray.get(5));
        assertEquals(hotspots.get(4), originalArray.get(1));
        assertEquals(hotspots.get(5), originalArray.get(0));
    }

    @Test
    public void TestSortRetailerByDistanceFromPoint() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers("src/test/resources/Lower_Manhattan_Retailers.csv");
        ArrayList<RetailerLocation> orignalList = CSVLoader.populateRetailers("src/test/resources/Lower_Manhattan_Retailers.csv");
        for(int i = retailers.size() -1; i > 4; i--){ // cut the list to a more managable size backwards for efficancy
            retailers.remove(i);
            orignalList.remove(i);
        }
        Point.Float testPoint = new Point2D.Float((float)-73.96753849952732,(float)40.76045675959568);
        DataAnalyser.sortRetailerByDistanceFromPoint(retailers,testPoint);
        assertEquals(retailers.get(0), orignalList.get(4));
        assertEquals(retailers.get(1), orignalList.get(2));
        assertEquals(retailers.get(2), orignalList.get(3));
        assertEquals(retailers.get(3), orignalList.get(1));
        assertEquals(retailers.get(4), orignalList.get(0));

    }
}