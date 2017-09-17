package seng202.team1;


;
import org.junit.Test;
import seng202.team1.DataAnalysis.DataAnaliser;
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
        ArrayList<BikeTrip> results = DataAnaliser.searchBikeTrips(40.732,-73.9925,600); // should be in the middle of the test data
        assertEquals(2, results.size());
    }

    @Test
    public void TestSearchWifiPoints(){ //Needs better data to be checked with #TODO
        ArrayList<WifiPoint> results = DataAnaliser.searchWifiPoints(40.755, -73.985,600);
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
        assertEquals(testData.get(0), oldData.get(2));
        assertEquals(testData.get(1), oldData.get(0));
        assertEquals(testData.get(2), oldData.get(1));
    }

    @Test
    public void TestPrimaryFunctions(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers("ret.csv");
        ArrayList<String> primaryFunctions = DataAnaliser.generatePrimaryFunctionsList(retailers);
        assertEquals(3,primaryFunctions.size());
    }
    @Test
    public void TestPrimaryFunctionsLargeList(){
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers("Lower_Manhattan_Retailers.csv");
        ArrayList<String> primaryFunctions = DataAnaliser.generatePrimaryFunctionsList(retailers);
        assertEquals(9,primaryFunctions.size());
    }
/*
/*
    @Test
    public void TestFindClosestWifiPointToStartPointExists(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip testTrip = bikeTrips.get(1);
        WifiPoint testPoint = DataAnaliser.findClosestWifiToBikeRouteStart(testTrip);
        assertEquals(); // can't test just yet as need the point class the Ollie is working on
    }*/
}
