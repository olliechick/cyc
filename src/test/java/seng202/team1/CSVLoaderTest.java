package seng202.team1;

import junit.framework.TestCase;
import org.apache.commons.csv.CSVRecord;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import static seng202.team1.CSVLoader.populateBikeTrips;
import static seng202.team1.CSVLoader.populateRetailers;
import static seng202.team1.CSVLoader.populateWifiHotspots;


public class CSVLoaderTest extends TestCase {

    String csv_resource_dir = "src/test/resources/";

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
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
        String filename = csv_resource_dir + "testBiketrip.csv";
        ArrayList<BikeTrip> trips = populateBikeTrips(filename);
        BikeTrip modelBikeTrip = new BikeTrip(551,
                LocalDateTime.of(2015, Month.DECEMBER, 1, 8, 8, 53),
                LocalDateTime.of(2015, Month.DECEMBER, 1, 8, 18, 05),
                new Point.Float((float) 40.76727216, (float) -73.99392888),
                new Point.Float((float) 40.75992262, (float) -73.97648516), 22307, 'm', 1980);
        assertEquals(modelBikeTrip, trips.get(0));
    }

    public void testPopulateWifiHotspots() throws Exception {
        String filename = csv_resource_dir + "testWifi.csv";
        ArrayList<WifiPoint> wifiSpots = populateWifiHotspots(filename);
        WifiPoint modelWifiHotspot = new WifiPoint("334", "POINT (-73.74567356126445 40.67695272804687)", "QU", "Free", "40.676952728", "-73.7456735613", "134-26 225 STREET", "QPL");
        assertEquals(modelWifiHotspot, wifiSpots.get(0));

    }

    public void testPopulateRetailers() throws Exception {
        String filename = csv_resource_dir + "testRetailers.csv";
        ArrayList<RetailerLocation> retailers = populateRetailers(filename);
        RetailerLocation modelRetailer = new RetailerLocation("Candy Plus", "16 Beaver Street, New York, NY 10004", "Shopping", "Candy & Chocolate");
        assertEquals(modelRetailer, retailers.get(0));
    }

}