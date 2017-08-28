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
    public void TestDistanceBikeTrips(){
        ArrayList<DataPoint> testData = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        BikeTrip trip1 = (BikeTrip) testData.get(1);
        BikeTrip trip2 = (BikeTrip) testData.get(2);
        double distance = DataAnaliser.calculateDistFromLatandLong(trip1,trip2);
        System.out.println(distance);
        assertEquals(distance,608.0 , 10.0);

    }
}
