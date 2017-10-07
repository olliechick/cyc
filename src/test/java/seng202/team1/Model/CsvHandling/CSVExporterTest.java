package seng202.team1.Model.CsvHandling;

import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.awt.Point;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportBikeTrips;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportCSV;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportRetailers;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportWifiHotspots;

/**
 * @author Ollie Chick
 */
public class CSVExporterTest {

    private final static String OUTPUT_DIR = "src/test/resources/";
    private final static String OUTPUT_FILE = OUTPUT_DIR + "testoutput.csv";
    private final static String username = "testUser";


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Make sure the directory is there for the file
        File file = new File(OUTPUT_DIR);
        file.mkdirs();
    }

/*
    @Before
    public void setUp() throws Exception {
        // Set up database to store data points in
        try {
            DatabaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
*/

    @Test
    public void testExportCSV() throws Exception {
        // Generate test input
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList("line1e1,line1e2", "line2e1,line2e2"));

        // Export it to file (this is what is being tested)
        exportCSV(OUTPUT_FILE, lines);

        // Load it back in using CSVLoader
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(OUTPUT_FILE);

        //Check each entry is as expected
        assertEquals("line1e1", records.get(0).get(0));
        assertEquals("line1e2", records.get(0).get(1));
        assertEquals("line2e1", records.get(1).get(0));
        assertEquals("line2e2", records.get(1).get(1));

        //Check there are 2 entries
        assertEquals(2, records.size());
    }


    @Test
    public void testExportBikeTrips() throws Exception {
        int duration = 10;
        LocalDateTime startTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        Point.Float startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        Point.Float endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        int bikeID = 1;
        char gender = 'f';
        int birthYear = 2000;
        Double tripDistance = 12.0;

        BikeTrip bikeTrip = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender, birthYear, tripDistance);

        String username = "testUser";

        // Save bike trip to the database
        try {
            DatabaseManager.addRecord(bikeTrip, username, "csvTestExportBikeTrips");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Export it to file (this is what is being tested)
        exportBikeTrips(OUTPUT_FILE, username);

        // Load it back in using CSVLoader
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips(OUTPUT_FILE);

        assertEquals(bikeTrip, bikeTrips.get(0)); //check the bike trip is the same
        assertEquals(1, bikeTrips.size()); //check there is only one
    }


    @Test
    public void testExportBikeTripsWithNulls() throws Exception {
        int duration = 10;
        LocalDateTime startTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        Point.Float startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        Point.Float endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        int bikeID = -1;
        char gender = 'u';
        int birthYear = -1;
        Double tripDistance = 12.0;
        boolean isUserDefinedPoint = false;

        BikeTrip bikeTrip = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender, birthYear, tripDistance);

        // Save bike trip to the database
        try {
            DatabaseManager.addRecord(bikeTrip, username, "csvTestExportBikeTripsWithNulls");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Export it to file (this is what is being tested)
        exportBikeTrips(OUTPUT_FILE, username);

        // Load it back in using CSVLoader
        ArrayList<BikeTrip> bikeTrips = CSVLoader.populateBikeTrips(OUTPUT_FILE);

        assertEquals(bikeTrip, bikeTrips.get(0)); //check the bike trip is the same
        assertEquals(1, bikeTrips.size()); //check there is only one
    }

    @Test
    public void testExportWifiHotspots() throws Exception {

        int objectId = 998;
        Point.Float coords = new Point.Float((float) -73.994039, (float) 40.745968);
        String name = "mn-05-123662";
        String location = "179 WEST 26 STREET";
        String locationType = "Outdoor Kiosk";
        String hood = "Midtown-Midtown South";
        String borough = "Manhattan";
        String city = "New York";
        int zipcode = 10001;
        String cost = "Free";
        String provider = "LinkNYC - Citybridge";
        String remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        String ssid = "LinkNYC Free Wi-Fi";
        String sourceId = "LINK-00869";
        LocalDateTime datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);

        WifiPoint wifiPoint = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated);

        // Save wifi point to the database
        try {
            DatabaseManager.addRecord(wifiPoint, username, "csvTestExportWifiHotspots");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Export it to file (this is what is being tested)
        exportWifiHotspots(OUTPUT_FILE, username);

        // Load it back in using CSVLoader
        ArrayList<WifiPoint> wifiPoints = CSVLoader.populateWifiHotspots(OUTPUT_FILE);

        assertEquals(wifiPoint, wifiPoints.get(0)); //check the wifi point is the same
        assertEquals(1, wifiPoints.size()); //check there is only one

    }

    @Test
    public void testExportWifiHotspotsWithNulls() throws Exception {

        int objectId = -1;
        Point.Float coords = new Point.Float((float) -73.994039, (float) 40.745968);
        String name = null;
        String location = null;
        String locationType = null;
        String hood = "Midtown-Midtown South";
        String borough = "Manhattan";
        String city = "New York";
        int zipcode = 10001;
        String cost = "Free";
        String provider = "LinkNYC - Citybridge";
        String remarks = null;
        String ssid = "LinkNYC Free Wi-Fi";
        String sourceId = null;
        LocalDateTime datetimeActivated = null;

        WifiPoint wifiPoint = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated);

        // Save wifi point to the database
        try {
            DatabaseManager.addRecord(wifiPoint, username, "csvTestExportWifiHotspotsWithNulls");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Export it to file (this is what is being tested)
        exportWifiHotspots(OUTPUT_FILE, username);

        // Load it back in using CSVLoader
        ArrayList<WifiPoint> wifiPoints = CSVLoader.populateWifiHotspots(OUTPUT_FILE);

        assertEquals(wifiPoint, wifiPoints.get(0)); //check the wifi point is the same
        assertEquals(1, wifiPoints.size()); //check there is only one

    }

    @Test
    public void testExportRetailers() throws Exception {
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

        RetailerLocation retailer = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords);

        // Save wifi point to the database
        try {
            DatabaseManager.addRecord(retailer, username, "csvTestExportRetailers");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Export it to file (this is what is being tested)
        exportRetailers(OUTPUT_FILE, username);

        // Load it back in using CSVLoader
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(OUTPUT_FILE);

        assertEquals(retailer, retailers.get(0)); //check the wifi point is the same
        assertEquals(1, retailers.size()); //check there is only one
    }

    @Test
    public void testExportRetailersWithNull() throws Exception {
        String name = "Pearl Bodywork";
        String addressLine1 = null;
        String addressLine2 = null;
        String city = "New York";
        String state = "NY";
        int zipcode = -1;
        String blockLot = null;
        String primaryFunction = "Personal and Professional Services";
        String secondaryFunction = "Spa";
        Point.Float coords = null;

        RetailerLocation retailer = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords);

        // Save wifi point to the database
        try {
            DatabaseManager.addRecord(retailer, username, "csvTestExportRetailersWithNulls");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Export it to file (this is what is being tested)
        exportRetailers(OUTPUT_FILE, username);

        // Load it back in using CSVLoader
        ArrayList<RetailerLocation> retailers = CSVLoader.populateRetailers(OUTPUT_FILE);

        assertEquals(retailer, retailers.get(0)); //check the wifi point is the same
        assertEquals(1, retailers.size()); //check there is only one
    }


    @After
    public void tearDown() throws Exception {/*
        // Close and delete database
        try {
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        DatabaseManager.deleteDatabase();
    }


    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Delete file that the test output was in
        File file = new File(OUTPUT_FILE);
        file.delete();
    }


}