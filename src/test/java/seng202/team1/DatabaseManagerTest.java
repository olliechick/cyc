package seng202.team1;

import org.junit.*;

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

        retailer = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        trip = new BikeTrip(startTime, stopTime, startPoint, endPoint, bikeID,
                gender, birthYear, isUserDefinedPoint);
    }

    @Before
    public void SetUp() {
        DatabaseManager.connect();


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
    public void addRetailerRecord() {
        try {
            DatabaseManager.addRecord(retailer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1, DatabaseManager.getNumberOfRowsFromType(RetailerLocation.class));
    }

    @Test
    public void addBikeTrip() {

        try {
            DatabaseManager.addBikeTrip(trip);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1, DatabaseManager.getNumberOfBikeTrips());

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
