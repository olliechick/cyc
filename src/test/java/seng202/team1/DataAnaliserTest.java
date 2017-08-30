package seng202.team1;


;
import org.junit.Test;
import seng202.team1.DataAnalysis.DataAnaliser;
import java.util.ArrayList;

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
        System.out.println(distance);
        assertEquals(distance,608.0 , 10.0);

    }

    @Test
    public void TestDistanceBikeTripsSame(){
        ArrayList<BikeTrip> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip trip1 = testData.get(1);
        BikeTrip trip2 = testData.get(1);
        double distance = DataAnaliser.calculateDistOfBikeTrips(trip1,trip2);
        System.out.println(distance);
        assertEquals(distance,981 , 10.0);

    }

    @Test
    public void TestSearchBikeTripsMatching(){ //Needs better data to be checked with #TODO
        ArrayList<BikeTrip> results = DataAnaliser.searchBikeTrips(40.732,-73.9925,600); // should be in the middle of the test data
        assertEquals(2, results.size());
    }

    @Test
    public void TestSearchWifiPoints(){ //Needs better data to be checked with #TODO
        ArrayList<WifiPoint> results = DataAnaliser.searchWifiPoints(40.755, -73.985,500);
        assertEquals(2, results.size());
    }

    @Test
    public void TestFindClosestWifiPointToStartPointExists(){
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip testTrip = bikeTrips.get(1);
        WifiPoint testPoint = DataAnaliser.findClosestWifiToBikeRouteStart(testTrip);
        assertEquals(); // can't test just yet as need the point class the Ollie is working on
    }
}
