package seng202.team1;

import org.junit.Test;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class InputValidatorTest {

    String filePath = "src/test/resources/";
    @Test
    public void TestIsDuplicateBikeTripTrue() throws Exception {
        ArrayList<BikeTrip> trips = CSVLoader.populateBikeTrips(filePath + "testBiketrip.csv");
        BikeTrip trip = trips.get(2);
        assertEquals(true, InputValidator.isDuplicateBikeTrip(trip, trips));
    }

    @Test
    public void TestIsDuplicateBikeTripFalse() throws Exception {
        ArrayList<BikeTrip> trips = CSVLoader.populateBikeTrips(filePath + "testBiketrip.csv");

        LocalDateTime startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        int duration = 10;
        Point.Float startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        Point.Float endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        int bikeID = 1;
        char gender = 'f';
        int birthYear = 2000;
        boolean isUserDefinedPoint = false;

        BikeTrip trip = new BikeTrip(duration, startTime, stopTime, startPoint,
                endPoint, bikeID, gender, birthYear, isUserDefinedPoint);
        assertEquals(false, InputValidator.isDuplicateBikeTrip(trip, trips));
    }

    @Test
    public void TestIsDuplicateWifiPointTrue() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(filePath+"testWifi.csv");
        WifiPoint hotspot = hotspots.get(3);
        assertEquals(true, InputValidator.isDuplicateWifiPoint(hotspot,hotspots));
    }

    @Test
    public void TestIsDuplicateWifiPointFalse() throws Exception {
        ArrayList<WifiPoint> hotspots = CSVLoader.populateWifiHotspots(filePath+"testWifi.csv");
        int objectId = 998;
        Point.Float coords = new Point.Float((float) -73.994039, (float) 40.745968);
        String name = "mn-05-123662";
        String location = "179 WEST 26 STREET";
        String locationType = "Outdoor Kiosk";
        String hood = "Midtown-Midtown South";
        String borough = "Manhattan";
        String city = "New York";
        int zipcode = 10001;
        String  cost = "Free";
        String provider = "LinkNYC - Citybridge";
        String remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        String  ssid = "LinkNYC Free Wi-Fi";
        String  sourceId = "LINK-00869";
        LocalDateTime datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        boolean isUserDefinedPoint = false;

        WifiPoint hotspot = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        assertEquals(false, InputValidator.isDuplicateWifiPoint(hotspot,hotspots));
    }

    @Test
    public void TestIsDuplicateRetailerTrue() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(filePath+"testRetailers.csv");
        RetailerLocation shop = retailers.get(11);
        assertEquals(true, InputValidator.isDuplicateRetailer(shop, retailers));
    }

    @Test
    public void TestIsDuplicateRetailerFalse() throws Exception {
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(filePath+"testRetailers.csv");

        String name = "Pearl Bodywork";
        String addressLine1 = "60 Pearl Street";
        String addressLine2 = "Floor 2";
        String city = "New York";
        String state = "NY";
        int zipcode = 10004;
        String blockLot = "7-38";
        String primaryFunction = "Personal and Professional Services";
        String secondaryFunction = "Spa";
        boolean isUserDefinedPoint = true;
        Point.Float coords = new Point.Float((float) -74.011071, (float) 40.723417);
        RetailerLocation shop = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        assertEquals(false, InputValidator.isDuplicateRetailer(shop, retailers));
    }

}