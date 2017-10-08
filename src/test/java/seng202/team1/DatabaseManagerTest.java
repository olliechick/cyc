package seng202.team1;

import org.junit.*;
import seng202.team1.Model.*;

import javax.xml.crypto.Data;
import java.awt.Point;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for DatabaseManager class.
 *
 * @author Ridge Nairn
 */


public class DatabaseManagerTest {

    private static BikeTrip trip;
    private static BikeTrip trip2;
    private static RetailerLocation retailer;
    private static RetailerLocation retailer2;
    private static WifiPoint wifi;
    private static WifiPoint wifi2;
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
        retailer2 = new RetailerLocation("otherRetailer", addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords);
        trip = new BikeTrip(startTime, stopTime, startPoint, endPoint, bikeID,
                gender, birthYear);
        trip2 = new BikeTrip(startTime, stopTime, startPoint, endPoint, 9339,
                gender, birthYear);
        wifi = new WifiPoint(objectID, wificoords, placeName, location, locationType, hood,
                borough, city, zipcode, cost, provider, remarks, ssid, sourceID, startTime);
        wifi2 = new WifiPoint(objectID, wificoords, placeName, location, locationType, hood,
                borough, city, zipcode, cost, provider, remarks, "NewSSID", sourceID, startTime);
    }


    @Before
    public void SetUp() throws Exception{
        DatabaseManager.open();
    }

    @After
    public void TearDown() throws Exception{
        DatabaseManager.close();
        DatabaseManager.deleteDatabase();
    }

    @Test
    public void createNewDatabase() throws Exception {
        Assert.assertTrue(DatabaseManager.isDatabaseConnected());
    }

    @Test
    public void addWifiPointToDifferentList() throws Exception {

        DatabaseManager.addRecord(wifi, model.getUserName(), "NotMyWifi");
        DatabaseManager.addRecord(wifi, model.getUserName(), "myWifi");

        System.out.println(DatabaseManager.getListID(model.getUserName(), "myWifi", WifiPointList.class));
        assertEquals(wifi, DatabaseManager.getWifiPoints(model.getUserName(), "myWifi").get(0));
    }

    @Test
    public void addRetailerPoint() throws Exception {
        try {
            DatabaseManager.addRecord(retailer, model.getUserName(), "myRetailers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(retailer, DatabaseManager.getRetailers(model.getUserName(), "myRetailers").get(0));
    }

    @Test
    public void addWifiPoint() throws Exception {

        DatabaseManager.addRecord(wifi, model.getUserName(), "myWifi");


        System.out.println(DatabaseManager.getListID(model.getUserName(), "myWifi", WifiPointList.class));
        assertEquals(wifi, DatabaseManager.getWifiPoints(model.getUserName(), "myWifi").get(0));
    }

    @Test
    public void addWifiPointToSameList() throws Exception {

        DatabaseManager.addRecord(wifi, model.getUserName(), "NotMyWifi");
        DatabaseManager.addRecord(wifi, model.getUserName(), "myWifi");
        DatabaseManager.addRecord(wifi, model.getUserName(), "myWifi");
        DatabaseManager.addRecord(wifi, "OtherName", "AlsoNotMyWifi");
        DatabaseManager.addRecord(wifi, model.getUserName(), "MYWIFI"); // Different case

        ArrayList<WifiPoint> wifiPoints = DatabaseManager.getWifiPoints(model.getUserName(), "myWifi");

        for (WifiPoint wifiPoint : wifiPoints) {
            System.out.println(wifiPoint.getName());
        }
        assertEquals(2, DatabaseManager.getWifiPoints(model.getUserName(), "myWifi").size());
    }

    @Test
    public void addBikeTrip() throws Exception {

        DatabaseManager.addRecord(trip, model.getUserName(), "myBikeTrips");

        System.out.println(trip.getBikeId());
        assertEquals(trip, DatabaseManager.getBikeTrips(model.getUserName(), "myBikeTrips").get(0));
    }

    @Test
    public void getOnlyUserTrips() throws Exception {

        DatabaseManager.addRecord(trip, model.getUserName(), "myTrips");
        DatabaseManager.addRecord(trip, model.getUserName(), "myTrips");

        DatabaseManager.addRecord(trip, "notMyUser", "myTrips"); // Wrong user
        DatabaseManager.addRecord(trip, model.getUserName(), "NotMyTrips"); // Wrong list
        assertEquals(2, DatabaseManager.getBikeTrips(model.getUserName(), "myTrips").size());
    }

    @Test
    public void getItemsOfSameListNameOfDifferentType() throws Exception {

        DatabaseManager.addRecord(trip, model.getUserName(), "testList");
        DatabaseManager.addRecord(wifi, model.getUserName(), "testList");

        assertEquals(1, DatabaseManager.getBikeTrips(model.getUserName(), "testList").size());
        assertEquals(1, DatabaseManager.getWifiPoints(model.getUserName(), "testList").size());

    }

    @Test
    public void getLists() throws Exception {

        DatabaseManager.addRecord(trip, model.getUserName(), "testList0"); // Wrong type
        DatabaseManager.addRecord(wifi, model.getUserName(), "testList1"); // YES
        DatabaseManager.addRecord(wifi, model.getUserName(), "testList2"); // YES
        DatabaseManager.addRecord(retailer, model.getUserName(), "testList3"); // Wrong type
        DatabaseManager.addRecord(wifi, "otherUser", "DifferentUser"); // Wrong user

        ArrayList<String> lists = DatabaseManager.getLists(model.getUserName(), WifiPointList.class);
        List<String> expected = Arrays.asList("testList1", "testList2");
        assertEquals(expected, lists);
    }

    @Test
    public void updateBikeTrip() throws Exception {
        DatabaseManager.addRecord(trip, model.getUserName(), "update");
        DatabaseManager.updatePoint(model.getUserName(), "update", trip, trip2);
        BikeTripList trips = new BikeTripList("update", DatabaseManager.getBikeTrips(model.getUserName(), "update"));
        assertEquals(1, trips.getBikeTrips().size());
        assertEquals(trip2, trips.getBikeTrips().get(0));
    }

    @Test
    public void updateWifiPoint() throws Exception {
        DatabaseManager.addRecord(wifi, model.getUserName(), "update");
        DatabaseManager.updatePoint(model.getUserName(), "update", wifi, wifi2);
        WifiPointList wifiPointList = new WifiPointList("update", DatabaseManager.getWifiPoints(model.getUserName(), "update"));
        assertEquals(1, wifiPointList.getWifiPoints().size());
        assertEquals(wifi2, wifiPointList.getWifiPoints().get(0));
    }

    @Test
    public void updateRetailerLocation() throws Exception {
        DatabaseManager.addRecord(retailer, model.getUserName(), "update");
        DatabaseManager.updatePoint(model.getUserName(), "update", retailer, retailer2);
        RetailerLocationList retailerLocationList = new RetailerLocationList("update", DatabaseManager.getRetailers(model.getUserName(), "update"));
        assertEquals(1, retailerLocationList.getRetailerLocations().size());
        assertEquals(retailer2, retailerLocationList.getRetailerLocations().get(0));
    }
}
