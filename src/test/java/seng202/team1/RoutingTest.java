package seng202.team1;

import org.junit.Before;
import org.junit.Test;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.Google.Routing;
import seng202.team1.Model.BikeTrip;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jbu71 on 22/09/17.
 */
public class RoutingTest {
    ArrayList<BikeTrip> bikeTrips;
    ArrayList<BikeTrip> orignalList;

    @Before
    public void setUp() throws Exception {
        bikeTrips = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
        //orignalList = CSVLoader.populateBikeTrips("bikeTripTestData.csv");
    }

    @Test
    public void findCommonTripsFromPosition() throws Exception {
        Point.Float testPoint = new Point.Float((float)-73.99492888,(float)40.745968);
        ArrayList<BikeTrip> results = Routing.findCommonTripsFromPosition(testPoint, bikeTrips);
        assertEquals(results.get(0), bikeTrips.get(3));

    }



}