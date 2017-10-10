package seng202.team1;

import junit.framework.TestCase;
import org.apache.commons.csv.CSVRecord;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateBikeTrips;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateRetailers;
import static seng202.team1.Model.CsvHandling.CSVLoader.populateWifiHotspots;


public class CSVLoaderTest extends TestCase {

    private String csv_resource_dir = "src/test/resources/";

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLoadCSVOneEntry() throws Exception {
        String filename = csv_resource_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename, 1);
        assertEquals(1, records.size());
    }

    public void testLoadCSVExtraEntries() throws Exception {
        String filename = csv_resource_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename, 10);
        assertEquals(2, records.size());
    }

    public void testLoadCSVSecondEntry() throws Exception {
        String filename = csv_resource_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename, 1, 1);
        assertEquals(1, records.size());
        assertTrue(records.get(0).get(0).equals("l2c1"));
        assertTrue(records.get(0).get(1).equals("l2c2"));
    }

    public void testLoadCSVSecondEntryOverrun() throws Exception {
        String filename = csv_resource_dir + "testfile.csv";
        try {
            ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename, 10, 1);
            fail(); //should have thrown an error
        } catch (IllegalArgumentException e) {
            assertEquals("There are not 2 blocks.", e.getMessage());
        }

    }

    public void testLoadCSVAllEntries() throws Exception {
        String filename = csv_resource_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename);
        assertEquals(2, records.size());
    }

    public void testLoadCSVContents() throws Exception {
        String filename = csv_resource_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename);

        /* This would be better done by creating a model ArrayList
           of CSVRecords and comparing it to that,
           but I can't find a way to create a custom CSVRecord. --Ollie
        */

        //test contents of each cell
        assertTrue(records.get(0).get(0).equals("l1c1"));
        assertTrue(records.get(0).get(1).equals("l1c2"));
        assertTrue(records.get(1).get(0).equals("l2c1"));
        assertTrue(records.get(1).get(1).equals("l2c2"));

        //test that a cell doesn't equal just anything
        assertFalse(records.get(0).get(0).equals("some random string"));
    }

    public void testPopulateBikeTrips() throws Exception {
        String filename = csv_resource_dir + "testBiketrips.csv";
        ArrayList<BikeTrip> trips = populateBikeTrips(filename);
        BikeTrip modelBikeTrip = new BikeTrip(551,
                LocalDateTime.of(2015, Month.DECEMBER, 1, 8, 8, 53),
                LocalDateTime.of(2015, Month.DECEMBER, 1, 8, 18, 5),
                new Point.Float((float) -73.99392888, (float) 40.76727216),
                new Point.Float((float) -73.97648516, (float) 40.75992262),
                22307, 'm', 1980);
        assertEquals(modelBikeTrip, trips.get(0));
    }

    public void testPopulateWifiHotspots() throws Exception {
        String filename = csv_resource_dir + "testWifi.csv";
        ArrayList<WifiPoint> wifiSpots = populateWifiHotspots(filename);
        WifiPoint modelWifiHotspot = new WifiPoint(334,
                new Point.Float((float) -73.74567356126445, (float) 40.6769527280),
                "Laurelton", "134-26 225 STREET",
                "Library", "Laurelton", "Queens",
                "Laurelton", 11413, "Free", "QPL",
                "", "QBPL_WIRELESS", "",  null);
        assertEquals(modelWifiHotspot, wifiSpots.get(0));

    }

    public void testPopulateRetailers() throws Exception {
        String filename = csv_resource_dir + "testRetailers.csv";
        ArrayList<RetailerLocation> retailers = populateRetailers(filename);
        RetailerLocation modelRetailer = new RetailerLocation("Candy Plus",
                "16 Beaver Street", "", "New York",
                "NY" , 10004, "11-7", "Shopping",
                "Candy & Chocolate",
                new Point.Float((float) -74.0125066, (float) 40.7048083));
        assertEquals(modelRetailer, retailers.get(0));
    }

}