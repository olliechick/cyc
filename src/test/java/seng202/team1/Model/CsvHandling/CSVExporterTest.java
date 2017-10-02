package seng202.team1.Model.CsvHandling;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import seng202.team1.Model.BikeTrip;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static seng202.team1.Model.CsvHandling.CSVExporter.exportCSV;

/**
 * @author Ollie Chick
 */
public class CSVExporterTest {

    private final static String OUTPUT_FILE = "src/test/resources/testoutput.csv";

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
        boolean isUserDefinedPoint = false;

        BikeTrip bikeTrip = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender, birthYear, tripDistance);

        String username = "testUser";


    }

    @Test
    public void testExportWifiHotspots() throws Exception {

    }

    @Test
    public void testExportRetailers() throws Exception {

    }

}