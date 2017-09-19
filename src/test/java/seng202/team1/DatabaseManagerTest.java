package seng202.team1;

import org.junit.*;

import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Unit tests for DatabaseManager class.
 * @author Ridge Nairn
 */


public class DatabaseManagerTest {

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
        RetailerLocation retailer = new RetailerLocation("Barnes and Noble", "555 5th Avenue, New York, NY 10017", "", "");
        try {
            DatabaseManager.addRecord(retailer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(1, DatabaseManager.getNumberOfRowsFromType(RetailerLocation.class));
    }

    @Test
    public void addBikeTrip() {
        BikeTrip trip = new BikeTrip("1.5hrs","2017-01-01 12:00", "2017-01-01 13:30", "startLatitude", "startLongitude", "endLatitude", "endLongitude");
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
            BikeTrip trip = new BikeTrip("1.5hrs", "2017-01-01 12:00", "2017-01-01 13:30", "startLatitude", "startLongitude", "endLatitude", "endLongitude");
            try {
                DatabaseManager.addBikeTrip(trip);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        assertEquals(100, DatabaseManager.getNumberOfBikeTrips());
    }
}
