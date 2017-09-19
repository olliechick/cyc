package seng202.team1;

import org.junit.*;

import javax.xml.crypto.Data;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * Unit tests for DatabaseManager class.
 * @author Ridge Nairn
 */


public class DatabaseManagerTest {

    private static BikeTrip trip;
    private static RetailerLocation retailer;
    private static WifiPoint wifi;

    @BeforeClass
    public static void Initialise() {
        LocalDateTime startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        Point.Float startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        Point.Float endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        int bikeID = 1;
        char gender = 'f';
        int birthYear = 2000;
        boolean isUserDefinedPoint = false;

        String name = "Pearl Bodywork";
        String addressLine1 = "60 Pearl Street";
        String addressLine2 = "Floor 2";
        String city = "New York";
        String state = "NY";
        int zipcode = 10004;
        String blockLot = "7-38";
        String primaryFunction = "Personal and Professional Services";
        String secondaryFunction = "Spa";
        Point.Float coords = new Point.Float((float) -74.011071, (float) 40.703417);

        int objectID = 2566;
        Point.Float wificoords = new Point.Float((float) -73.98236686967641, (float) 40.7754432795939);
        String placeName = "Wifi Hotspot #424";
        String location = "The streets fam";
        String locationType = "Indoors";
        String hood = "Harlem";
        String borough = "Manhattan";
        String cost = "Free";
        String provider = "LinkNYC - Citybridge";
        String remarks = "They're probably stealing your data.";
        String ssid = "Free Wifi Hotspot";
        String sourceID = "";


        retailer = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        trip = new BikeTrip(startTime, stopTime, startPoint, endPoint, bikeID,
                gender, birthYear, isUserDefinedPoint);
        wifi = new WifiPoint(objectID, wificoords, placeName, location, locationType, hood,
                borough, city, zipcode, cost, provider, remarks, ssid, sourceID, startTime, isUserDefinedPoint);
    }

    @Before
    public void SetUp() {
        try {
            DatabaseManager.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @After
    public void TearDown() {
        DatabaseManager.deleteDatabase();
    }

    @Test
    public void createNewDatabase() {
        Assert.assertTrue(DatabaseManager.isDatabaseConnected());
    }

    @Test
    public void deleteDatabase() {
        DatabaseManager.deleteDatabase();
        assertFalse(DatabaseManager.isDatabaseConnected());
    }

    @Test
    public void addRetailerPoint() {
        try {
            DatabaseManager.addRecord(retailer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(retailer, DatabaseManager.getRetailers().get(0));
    }

    @Test
    public void addWifiPoint() {
        try {
            DatabaseManager.addRecord(wifi);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(wifi, DatabaseManager.getWifiPoints().get(0));
    }

    @Test
    public void addBikeTrip() {

        try {
            DatabaseManager.addBikeTrip(trip);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(trip, DatabaseManager.getAllTrips().get(0));
    }


    @Test
    public void get100BikeTrips() {
        for (int i = 0; i < 100; i++) {
            try {
                DatabaseManager.addBikeTrip(trip);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        assertEquals(100, DatabaseManager.getNumberOfBikeTrips());
    }



}
