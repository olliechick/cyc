package seng202.team1;

import junit.framework.TestCase;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import static seng202.team1.CSVLoader.populateBikeTrips;


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
        assertTrue(records.size() == 2);
    }

    public void testLoadCSVContents() throws Exception {
        String filename = base_dir + "testfile.csv";
        ArrayList<CSVRecord> records = CSVLoader.loadCSV(filename);
        assertTrue(records.get(0).get(0).equals("l1c1"));
        assertTrue(records.get(0).get(1).equals("l1c2"));
        assertTrue(records.get(1).get(0).equals("l2c1"));
        assertTrue(records.get(1).get(1).equals("l2c2"));
    }

    public void testPopulateBikeTrips() throws Exception {
        String filename = base_dir + "testBiketrip.csv";
        ArrayList<DataPoint> trips = populateBikeTrips(filename);
        BikeTrip modelBikeTrip = new BikeTrip("551", "12/1/2015 08:08:53", "12/1/2015 08:18:05", "-73.99392888", "40.76727216", "-73.97648516", "40.75992262");
        System.out.println(trips.get(0).equals(modelBikeTrip));
    }

    public void testPopulateWifiHotspots() throws Exception {
        
    }

    public void testPopulateRetailers() throws Exception {
    }

}