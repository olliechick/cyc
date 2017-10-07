package seng202.team1;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.awt.Point;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for DatabaseManager class.
 *
 * @author Ridge Nairn
 */


public class DatabaseManagerTest {

    private static BikeTrip trip;
    private static RetailerLocation retailer;
    private static WifiPoint wifi;
    private static UserAccountModel model;

    @BeforeClass
    public static void Initialise() {
        Main.createDirectories();

        LocalDateTime startTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        Point.Float startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        Point.Float endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        int bikeID = 1324;
        char gender = 'f';
        int birthYear = 2000;
        boolean isUserDefinedPoint = false;
        LocalDate birthdate = LocalDate.of(1997, 9, 15);

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

        model = new UserAccountModel(gender, birthdate, "TestAccount", "bad_password");

        retailer = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords);
        trip = new BikeTrip(startTime, stopTime, startPoint, endPoint, bikeID,
                gender, birthYear);
        wifi = new WifiPoint(objectID, wificoords, placeName, location, locationType, hood,
                borough, city, zipcode, cost, provider, remarks, ssid, sourceID, startTime);
    }

    /*
        @Before
        public void SetUp() {
            try {
                DatabaseManager.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    /*/
    @After
    public void TearDown() {/*
        try {
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        DatabaseManager.deleteDatabase();
    }

    @Test
    public void createNewDatabase() throws Exception {
        DatabaseManager.open();
        Assert.assertTrue(DatabaseManager.isDatabaseConnected());
        DatabaseManager.close();
    }

    @Test
    public void deleteDatabase() {
        DatabaseManager.deleteDatabase();
        assertFalse(DatabaseManager.isDatabaseConnected());
    }

    @Test
    public void addRetailerPoint() throws Exception {
        try {
            DatabaseManager.addRecord(retailer, model.getUserName(), "myRetailers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.open();
        assertEquals(retailer, DatabaseManager.getRetailers(model.getUserName(), "myRetailers").get(0));
        DatabaseManager.close();
    }

    @Test
    public void addWifiPoint() throws Exception {
        try {
            DatabaseManager.addRecord(wifi, model.getUserName(), "myWifi");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseManager.open();
        assertEquals(wifi, DatabaseManager.getWifiPoints(model.getUserName()).get(0));
        DatabaseManager.close();
    }

    @Test
    public void addBikeTrip() throws Exception {

        try {
            DatabaseManager.addRecord(trip, model.getUserName(), "myBikeTrips");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(trip.getBikeId());
        DatabaseManager.open();
        assertEquals(trip, DatabaseManager.getBikeTrips(model.getUserName()).get(0));
        DatabaseManager.close();
    }


    @Test
    public void getNumberOfBikeTrips() throws Exception {
        ArrayList<BikeTrip> trips = new ArrayList<>();

        // Add the same trip 100 times
        for (int i = 0; i < 100; i++) {
            trips.add(trip);
        }
        try {
            DatabaseManager.addRecords(trips, model.getUserName(), "lotsOfBikeTrips");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.open();
        assertEquals(100, DatabaseManager.getNumberOfRowsFromType(BikeTrip.class, model.getUserName()));
        DatabaseManager.close();
    }

    @Test
    public void getOnlyUserTrips() throws Exception {
        try {
            DatabaseManager.addRecord(trip, model.getUserName(), "myTrips");
            DatabaseManager.addRecord(trip, "notMyUser", "myTrips");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.open();
        assertEquals(1, DatabaseManager.getBikeTrips(model.getUserName()).size());
        DatabaseManager.close();
    }


}
