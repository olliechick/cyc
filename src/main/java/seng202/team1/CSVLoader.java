package seng202.team1;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class that will pull in our CSV files and then call the appropriate constructor.
 * Most likely will be static I'm thinking. Should allow us to use it without creating an instance.
 * Last updated 22 August 2017.
 * @author Josh Burt
 * @author Ollie Chick
 */
public class CSVLoader {


    final static String DEFAULT_BIKE_TRIPS_FILENAME = "src/main/resources/csv/biketrip.csv";
    final static String DEFAULT_WIFI_HOTSPOTS_FILENAME = "src/main/resources/csv/wifipoint.csv";
    final static String DEFAULT_RETAILER_LOCATIONS_FILENAME = "src/main/resources/csv/retailerlocation.csv";

    //Earliest date that a Wifi point can be set up -- any date before this will be replaced with null
    final static LocalDateTime EARLIEST_POSSIBLE_DATE = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0);


    /**
     * Takes a file named filename, which should be a csv, and returns an Arraylist of type CSVRecord.
     * This can be queried using the get(index) or get(columnName) [haven't figured out columnName though]
     *
     * @param filename The filename of the file to get data from.
     * @return CSVRecord
     * @throws IOException If an IO error occurs.
     *
     */
    public static ArrayList<CSVRecord> loadCSV(String filename) throws IOException {
        File csvData = new File(filename);
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
        ArrayList<CSVRecord> records = (ArrayList<CSVRecord>) parser.getRecords();
        return records;

    }


    /**
     * Calls the load CSV method and populates an array list with a set of
     * BikeTrip objects from the default filename.
     * If the file does not exist, then the error message is printed.
     * @return ArrayList<BikeTrip>
     */
    public static ArrayList<BikeTrip> populateBikeTrips() {
        return populateBikeTrips(DEFAULT_BIKE_TRIPS_FILENAME);
    }

    /**
     * Calls the load CSV method and populates an ArrayList with a set of BikeTrip objects from a given filename.
     * If the filename does not exist, then the error message is printed.
     * @param filename name of the file the data is to be loaded from.
     * @return ArrayList<BikeTrip>
     */
    public static ArrayList<BikeTrip> populateBikeTrips(String filename){ //
        ArrayList<BikeTrip> trips = new ArrayList<BikeTrip>();
        try {
            ArrayList<CSVRecord> tripData = loadCSV(filename);
            for (CSVRecord record : tripData){
                //First check it is a header row
                boolean isHeaderRow = false;
                try {
                    Integer.parseInt(record.get(11));
                } catch (NumberFormatException e) {
                    //Header row - bike id is not an int
                    isHeaderRow = true;
                }
                if (!isHeaderRow) {
                    long tripDuration = new Long(record.get(0));

                    // Set start time
                    LocalDateTime startTime;
                    try {
                        startTime = LocalDateTime.parse(record.get
                                (1), DateTimeFormatter.ofPattern
                                ("M/d/yyyy HH:mm:ss"));
                    } catch (Exception e) {
                        startTime = LocalDateTime.parse(record.get(1),
                                DateTimeFormatter.ofPattern
                                        ("yyyy-MM-dd HH:mm:ss"));
                    }

                    // Set stop time
                    LocalDateTime stopTime;
                    try {
                        stopTime = LocalDateTime.parse(record.get
                                (2), DateTimeFormatter.ofPattern
                                ("M/d/yyyy HH:mm:ss"));
                    } catch (Exception e) {
                        stopTime = LocalDateTime.parse(record.get(2),
                                DateTimeFormatter.ofPattern
                                ("yyyy-MM-dd HH:mm:ss"));
                    }

                    // Other stuff
                    Point.Float startPoint = new Point.Float(Float.parseFloat(record.get(6)), Float.parseFloat(record.get(5)));
                    Point.Float endPoint = new Point.Float(Float.parseFloat(record.get(10)), Float.parseFloat(record.get(9)));
                    int bikeID = Integer.parseInt(record.get(11));
                    char gender;
                    if (record.get(14).equals("1")) {
                        gender = 'm';
                    } else if (record.get(14).equals("2")) {
                        gender = 'f';
                    } else {
                        gender = 'u';
                    }
                    String birthYearString = record.get(13);
                    int birthYear;
                    if (birthYearString.isEmpty()) {
                        //unknown birth year flag
                        birthYear = -1;
                    } else {
                        birthYear = Integer.parseInt(birthYearString);
                    }
                    trips.add(new BikeTrip(tripDuration, startTime, stopTime, startPoint, endPoint, bikeID, gender,
                            birthYear, false));
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return trips;
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of
     * WifiPoint objects from the default filename.
     * If the filename does not exist, then the error message is printed.
     * @return ArrayList<WifiPoint>
     */
    public static ArrayList<WifiPoint> populateWifiHotspots() {
        return populateWifiHotspots(DEFAULT_WIFI_HOTSPOTS_FILENAME);
    }

    /**
     * Calls the load CSV method and populates an ArrayList with a set of WifiPoint objects from a given filename.
     * If the filename does not exist, then the error message is printed.
     * @param filename name of the file the data is to be loaded from.
     * @return ArrayList<WifiPoint>
     */
    public static ArrayList<WifiPoint> populateWifiHotspots(String filename){
        ArrayList<WifiPoint> wifiSpots = new ArrayList<WifiPoint>();
        try {
            ArrayList<CSVRecord> wifiData = loadCSV(filename);
            for(CSVRecord record : wifiData){
                //First check it is a header row
                boolean isHeaderRow = false;
                try {
                    Integer.parseInt(record.get(22));
                } catch (NumberFormatException e){
                    //Header row - zipcode is not an int
                    isHeaderRow = true;
                }
                if (!isHeaderRow) {
                    int objectId = Integer.parseInt(record.get(0));
                    Point.Float coords = new Point.Float(Float.parseFloat(record.get(8)), Float.parseFloat(record.get(7)));
                    ;
                    String name = record.get(5);
                    String location = record.get(6);
                    String locationType = record.get(11);
                    String hood = record.get(20);
                    String borough = record.get(18);
                    String city = record.get(13);
                    int zipcode = Integer.parseInt(record.get(22));
                    String cost = record.get(3);
                    String provider = record.get(4);
                    String remarks = record.get(12);
                    String ssid = record.get(14);
                    String sourceId = record.get(15);
                    LocalDateTime datetimeActivated = LocalDateTime.parse(record.get(16), DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss a Z"));
                    if (datetimeActivated.isBefore(EARLIEST_POSSIBLE_DATE)) {
                        // dates earlier than this means that this data is not available
                        datetimeActivated = null;
                    }
                    wifiSpots.add(new WifiPoint(objectId, coords, name, location, locationType, hood,
                            borough, city, zipcode, cost, provider, remarks, ssid, sourceId, datetimeActivated, false));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return wifiSpots;
    }


    /**
     * Calls the load CSV method and populates an array list with a set of
     * RetailerLocation objects from the default filename.
     * @return ArrayList<RetailerLocation>
     */
    public static ArrayList<RetailerLocation> populateRetailers() {
        return populateRetailers(DEFAULT_RETAILER_LOCATIONS_FILENAME);
    }

    /**
     * Calls the load CSV method and populates an array list with a set of
     * RetailerLocation objects.
     * If there are columns (9 and 10) for coords, these are loaded,
     * otherwise they are set to null.
     * @param filename name of the file the data is to be loaded from.
     * @return ArrayList<RetailerLocation>
     */
    public static ArrayList<RetailerLocation> populateRetailers(String filename){
        ArrayList<RetailerLocation> retailers = new ArrayList<RetailerLocation>(); //array list of retailers
        try {
            ArrayList<CSVRecord> retailerData = loadCSV(filename);
            for(CSVRecord record : retailerData){
                //First check it is a header row
                boolean isHeaderRow = false;
                try {
                    Integer.parseInt(record.get(5));
                } catch (NumberFormatException e){
                    //Header row - zipcode is not an int
                    isHeaderRow = true;
                }
                if (!isHeaderRow) {
                    String name = record.get(0);
                    String addressLine1 = record.get(1);
                    String addressLine2 = record.get(2);
                    String city = record.get(3);
                    String state = record.get(4);
                    int zipcode = Integer.parseInt(record.get(5));
                    String blockLot = record.get(6);
                    String primaryFunction = record.get(7);
                    String secondaryFunction = record.get(8);
                    // try to get coords
                    Point.Float coords =  new Point.Float();
                    try {
                        coords.y = Float.parseFloat(record.get(9)); //latitude
                        coords.x = Float.parseFloat(record.get(10)); //longitude
                    } catch (ArrayIndexOutOfBoundsException e){
                        //no such column as latitude/longitude
                        coords = null;
                    }
                    if (secondaryFunction.length() > 2) {
                        secondaryFunction = secondaryFunction.substring(2);
                    }
                    retailers.add(new RetailerLocation(name, addressLine1, addressLine2, city,
                            state, zipcode, blockLot, primaryFunction,
                            secondaryFunction, coords, false));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return retailers;
    }

    // Main class (for testing)
    /*public static void main(String[] args) {
        String filename = "wifiTester.csv";
        //populateBikeTrips(filename);

        ArrayList<DataPoint> wifis = populateWifiHotspots(filename);//populateBikeTrips(filename);
        for (DataPoint wifi : wifis) {
            System.out.println(wifi);
        }

        ArrayList<DataPoint> retailers = populateRetailers("ret.csv");//populateBikeTrips(filename);
        for (DataPoint retailer : retailers) {
            System.out.println(retailer);
        }

    }*/

}
