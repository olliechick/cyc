package seng202.team1;

import junit.framework.TestCase;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import static seng202.team1.CSVLoader.populateBikeTrips;
import static seng202.team1.CSVLoader.populateRetailers;
import static seng202.team1.CSVLoader.populateWifiHotspots;


public class CSVLoaderTest extends TestCase {

    String base_dir = "src/test/resources/";

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testLoadCSVAllEntries() throws Exception {
        String filename = base_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename);
        assertEquals(2, records.size());
    }

    public void testLoadCSVContents() throws Exception {
        String filename = base_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename);

        // This would be better done by creating a model ArrayList
        // of CSVRecords and comparing it to that,
        // but I can't find a way to create a custom CSVRecord. --Ollie

        //test contents of each cell
        assertTrue(records.get(0).get(0).equals("l1c1"));
        assertTrue(records.get(0).get(1).equals("l1c2"));
        assertTrue(records.get(1).get(0).equals("l2c1"));
        assertTrue(records.get(1).get(1).equals("l2c2"));

        //test that a cell doesn't equal just anything
        assertFalse(records.get(0).get(0).equals("some random string"));
    }

    public void testPopulateBikeTrips() throws Exception {
        String filename = base_dir + "testBiketrip.csv";
        ArrayList<DataPoint> trips = populateBikeTrips(filename);
        BikeTrip modelBikeTrip = new BikeTrip("551", "12/1/2015 08:08:53", "12/1/2015 08:18:05", "-73.99392888", "40.76727216", "-73.97648516", "40.75992262");
        assertEquals(modelBikeTrip, trips.get(0));
    }

    public void testPopulateWifiHotspots() throws Exception {
        String filename = base_dir + "testWifi.csv";
        ArrayList<DataPoint> wifiSpots = populateWifiHotspots(filename);
        WifiPoint modelWifiHotspot = new WifiPoint("334", "POINT (-73.74567356126445 40.67695272804687)", "QU", "Free", "40.676952728", "-73.7456735613", "134-26 225 STREET");
        assertEquals(modelWifiHotspot, wifiSpots.get(0));

    }

    public void testPopulateRetailers() throws Exception {
        String filename = base_dir + "testRetailers.csv";
        ArrayList<DataPoint> retailers = populateRetailers(filename);
        RetailerLocation modelRetailer = new RetailerLocation("Candy Plus", "16 Beaver Street, New York, NY 10004", "Shopping", "Candy & Chocolate");
        assertEquals(modelRetailer, retailers.get(0));
    }

}