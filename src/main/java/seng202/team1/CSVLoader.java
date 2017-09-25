package seng202.team1;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Class that can load CSV files and return ArrayLists of different subclasses of DataPoint.
 *
 * @author Josh Burt
 * @author Ollie Chick
 */
public class CSVLoader {

    private final static String CSV_DIRNAME = "/csv/";
    private final static String DEFAULT_BIKE_TRIPS_FILENAME = CSV_DIRNAME +
            "201512-citibike-tripdata_1000.csv";
    private final static String DEFAULT_WIFI_HOTSPOTS_FILENAME = CSV_DIRNAME +
            "NYC_Free_Public_WiFi_03292017.csv";
    private final static String DEFAULT_RETAILER_LOCATIONS_FILENAME = CSV_DIRNAME +
            "Lower_Manhattan_Retailers.csv";

    //Earliest date that a Wifi point can be set up -- any date before this will be replaced with null
    private final static LocalDateTime EARLIEST_POSSIBLE_DATE = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0);


    /**
     * Takes a file named filename, which should be a csv, and returns an ArrayList of type
     * CSVRecord.
     * This can be queried using the get(index) method.
     * This file should not be provided by the app (i.e. it should be provided
     * by the user at runtime).
     *
     * @param filename The filename of the file to get data from.
     * @return an ArrayList of CSVRecords of the each row of the csv
     * @throws IOException If an IO error occurs.
     */
    public static ArrayList<CSVRecord> loadCSV(String filename) throws IOException {
        return loadCSV(filename, true);
    }


    /**
     * Takes a file named filename, which should be a csv, and returns an ArrayList of type
     * CSVRecord.
     * This can be queried using the get(index) method.
     *
     * @param filename    The filename of the file to get data from.
     * @param isCustomCsv true if the file is a custom csv (i.e. not provided in the app)
     * @return an ArrayList of CSVRecords of the each row of the csv
     * @throws IOException If an IO error occurs.
     */
    public static ArrayList<CSVRecord> loadCSV(String filename, boolean isCustomCsv) throws IOException {
        // Opens the file in a different way depending on if the file is provided with the app
        // or by the user on runtime.
        if (isCustomCsv) {
            // Provided by user at runtime
            File csvData = new File(filename);
            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
            return (ArrayList<CSVRecord>) parser.getRecords();
        } else {
            // Provided by application
            InputStream csvData = FileInputStream.class.getResourceAsStream(filename);
            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
            return (ArrayList<CSVRecord>) parser.getRecords();
        }
    }


    /**
     * Takes a file named filename, which should be a csv, and returns an ArrayList of type
     * CSVRecord.
     * This can be queried using the get(index) method.
     * Only returns the first numberOfEntries entries (this may include header(s)).
     *
     * @param filename        The filename of the file to get data from.
     * @param numberOfEntries The number of entries to retrieve from the file.
     * @return an ArrayList of CSVRecords of the first numberOfEntries rows of the csv
     * @throws IOException              If an IO error occurs.
     * @throws IllegalArgumentException If there are not enough blocks in the file.
     */
    public static ArrayList<CSVRecord> loadCSV(String filename, int numberOfEntries) throws
            IOException, IllegalArgumentException {
        return loadCSV(filename, numberOfEntries, 0);
    }


    /**
     * Takes a file named filename, which should be a csv, and returns an ArrayList of
     * type CSVRecord.
     * This can be queried using the get(index) method.
     * Only returns the [blockNumber]th numberOfEntries entries (this may include header(s)).
     *
     * @param filename        The filename of the file to get data from.
     * @param numberOfEntries The number of entries to retrieve from the file.
     * @param blockNumber     The block number (e.g. 0 = first lot of n entries, 1 = second lot of n entries)
     * @return an ArrayList of CSVRecords of the [blockNumber]th numberOfEntries rows of the csv
     * @throws IOException              If an IO error occurs.
     * @throws IllegalArgumentException If there are not enough blocks in the file.
     */
    public static ArrayList<CSVRecord> loadCSV(String filename, int numberOfEntries, int blockNumber) throws
            IOException, IllegalArgumentException {
        File csvData = new File(filename);
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
        ArrayList<CSVRecord> records = (ArrayList<CSVRecord>) parser.getRecords();

        // Try to remove the entries before the block selected.
        try {
            records.subList(0, numberOfEntries * blockNumber).clear();
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("There are not " + (blockNumber + 1) + " blocks.");
        }

        // Try to trim it - if numberOfEntries > records.size(), do nothing
        try {
            records.subList(numberOfEntries * (blockNumber + 1), records.size()).clear();
        } catch (IllegalArgumentException e) {
            //there aren't that many items in the csv, so just return all of them
        }

        return records;
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of
     * BikeTrip objects from the default csv.
     * This csv will be provided with the app.
     *
     * @return an ArrayList of BikeTrips generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid bike trip
     */
    public static ArrayList<BikeTrip> populateBikeTrips() throws IOException, CsvParserException {
        return populateBikeTrips(DEFAULT_BIKE_TRIPS_FILENAME, false);
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of BikeTrip objects from a
     * given filename. This file should not be provided by the app (i.e. it should be provided
     * by the user at runtime).
     *
     * @param filename name of the file the data is to be loaded from.
     * @return an ArrayList of BikeTrips generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid bike trip
     */
    public static ArrayList<BikeTrip> populateBikeTrips(String filename) throws
            IOException, CsvParserException {
        return populateBikeTrips(filename, true);
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of BikeTrip objects from a
     * given filename. If the isCustomCsv flag is set, it will act as if the csv is in the app's
     * files.
     *
     * @param filename    name of the file the data is to be loaded from.
     * @param isCustomCsv true if the file is a custom csv (i.e. not provided in the app)
     * @return an ArrayList of BikeTrips generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid bike trip
     */
    public static ArrayList<BikeTrip> populateBikeTrips(String filename, boolean isCustomCsv) throws
            IOException, CsvParserException {
        ArrayList<BikeTrip> trips = new ArrayList<BikeTrip>();
        boolean isValidCsv = false; // assume false unless proven otherwise
        ArrayList<CSVRecord> tripData = loadCSV(filename, isCustomCsv);

        for (CSVRecord record : tripData) {
            // Process all the attributes - from most to least likely to fail
            try {
                // Set start time
                LocalDateTime startTime;
                try {
                    startTime = LocalDateTime.parse(record.get
                            (1), DateTimeFormatter.ofPattern
                            ("M/d/yyyy HH:mm:ss"));
                } catch (DateTimeParseException e) {
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
                } catch (DateTimeParseException e) {
                    stopTime = LocalDateTime.parse(record.get(2),
                            DateTimeFormatter.ofPattern
                                    ("yyyy-MM-dd HH:mm:ss"));
                }

                // Other stuff
                int bikeId = Integer.parseInt(record.get(11));

                int birthYear;
                String birthYearString = record.get(13);
                if (birthYearString.isEmpty()) {
                    //unknown birth year flag
                    birthYear = -1;
                } else {
                    birthYear = Integer.parseInt(birthYearString);
                }

                long tripDuration = new Long(record.get(0).trim());
                Point.Float startPoint = new Point.Float(Float.parseFloat(record.get(6)), Float.parseFloat(record.get(5)));
                Point.Float endPoint = new Point.Float(Float.parseFloat(record.get(10)), Float.parseFloat(record.get(9)));

                char gender;
                if (record.get(14).equals("1")) {
                    gender = 'm';
                } else if (record.get(14).equals("2")) {
                    gender = 'f';
                } else {
                    gender = 'u';
                }

                trips.add(new BikeTrip(tripDuration, startTime, stopTime, startPoint,
                        endPoint, bikeId, gender, birthYear, false));

                isValidCsv = true;
            } catch (Exception e) {
                // Some error processing the line - it's either a header field or the csv is invalid.
                // If this occurs for all lines in the CSV, a CsvParserException is thrown.
            }
        }
        if (!isValidCsv) {
            throw new CsvParserException(filename);
        }
        return trips;
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of
     * WifiPoint objects from the default csv.
     * This csv will be provided with the app.
     *
     * @return an ArrayList of WifiPoints generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid wifi point
     */
    public static ArrayList<WifiPoint> populateWifiHotspots() throws IOException,
            CsvParserException {
        return populateWifiHotspots(DEFAULT_WIFI_HOTSPOTS_FILENAME, false);
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of WifiPoint objects from a
     * given filename. This file should not be provided by the app (i.e. it should be provided
     * by the user at runtime).
     *
     * @param filename name of the file the data is to be loaded from.
     * @return an ArrayList of WifiPoints generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid bike trip
     */
    public static ArrayList<WifiPoint> populateWifiHotspots(String filename) throws
            IOException, CsvParserException {
        return populateWifiHotspots(filename, true);
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of WifiPoint objects from
     * a given filename. If the isCustomCsv flag is set, it will act as if the csv is in the app's
     * files.
     *
     * @param filename    name of the file the data is to be loaded from.
     * @param isCustomCsv true if the file is a custom csv (i.e. not provided in the app)
     * @return an ArrayList of WifiPoints generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid wifi point
     */
    public static ArrayList<WifiPoint> populateWifiHotspots(String filename, boolean isCustomCsv) throws
            IOException, CsvParserException {
        ArrayList<WifiPoint> wifiSpots = new ArrayList<WifiPoint>();
        boolean isValidCsv = false; // assume false unless proven otherwise
        ArrayList<CSVRecord> wifiData = loadCSV(filename, isCustomCsv);

        for (CSVRecord record : wifiData) {
            // Process all the attributes - from most to least likely to fail
            try {
                LocalDateTime datetimeActivated = LocalDateTime.parse(record.get(16), DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss a Z"));
                if (datetimeActivated.isBefore(EARLIEST_POSSIBLE_DATE)) {
                    // dates earlier than this means that this data is not available
                    datetimeActivated = null;
                }

                int zipcode = Integer.parseInt(record.get(22));
                int objectId = Integer.parseInt(record.get(0));
                Point.Float coords = new Point.Float(Float.parseFloat(record.get(8)), Float.parseFloat(record.get(7)));

                String name = record.get(5);
                String location = record.get(6);
                String locationType = record.get(11);
                String hood = record.get(20);
                String borough = record.get(18);
                String city = record.get(13);
                String cost = record.get(3);
                String provider = record.get(4);
                String remarks = record.get(12);
                String ssid = record.get(14);
                String sourceId = record.get(15);

                wifiSpots.add(new WifiPoint(objectId, coords, name, location, locationType, hood,
                        borough, city, zipcode, cost, provider, remarks, ssid, sourceId, datetimeActivated, false));

                isValidCsv = true;
            } catch (Exception e) {
                // Some error processing the line - it's either a header field or the csv is invalid.
                // If this occurs for all lines in the CSV, a CsvParserException is thrown.
            }
        }
        if (!isValidCsv) {
            throw new CsvParserException(filename);
        }
        return wifiSpots;
    }


    /**
     * Calls the load CSV method and populates an array list with a set of
     * RetailerLocation objects from the default csv.
     * This csv will be provided with the app.
     *
     * @return an ArrayList of RetailerLocations generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid retailer
     */
    public static ArrayList<RetailerLocation> populateRetailers() throws IOException,
            CsvParserException {
        return populateRetailers(DEFAULT_RETAILER_LOCATIONS_FILENAME, false);
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of RetailerLocation objects from a
     * given filename. This file should not be provided by the app (i.e. it should be provided
     * by the user at runtime).
     *
     * @param filename name of the file the data is to be loaded from.
     * @return an ArrayList of RetailerLocation generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid bike trip
     */
    public static ArrayList<RetailerLocation> populateRetailers(String filename) throws
            IOException, CsvParserException {
        return populateRetailers(filename, true);
    }


    /**
     * Calls the load CSV method and populates an ArrayList with a set of
     * RetailerLocation objects.
     * If there are columns (9 and 10) for co-ordinates, these are loaded;
     * otherwise, they are set to null.
     * If the isCustomCsv flag is set, it will act as if the csv is in the app's files.
     *
     * @param filename    name of the file the data is to be loaded from.
     * @param isCustomCsv true if the file is a custom csv (i.e. not provided in the app)
     * @return an ArrayList of RetailerLocations generated from the csv
     * @throws IOException        If an IO error occurs.
     * @throws CsvParserException if it can't find a single valid retailer
     */
    public static ArrayList<RetailerLocation> populateRetailers(String filename, boolean isCustomCsv) throws
            IOException, CsvParserException {
        ArrayList<RetailerLocation> retailers = new ArrayList<RetailerLocation>(); //array list of retailers
        boolean isValidCsv = false; // assume false unless proven otherwise
        ArrayList<CSVRecord> retailerData = loadCSV(filename, isCustomCsv);

        for (CSVRecord record : retailerData) {
            // Process all the attributes - from most to least likely to fail
            try {
                int zipcode = Integer.parseInt(record.get(5));

                // Try to get coords
                Point.Float coords = new Point.Float();
                try {
                    coords.y = Float.parseFloat(record.get(9)); //latitude
                    coords.x = Float.parseFloat(record.get(10)); //longitude
                } catch (ArrayIndexOutOfBoundsException e) {
                    //no such column as latitude/longitude
                    coords = null;
                }

                // Get secondary function
                String secondaryFunction = record.get(8);
                if (secondaryFunction.length() > 2) {
                    // Strip off the first bit. E.g. "F-Italian" -> "Italian"
                    secondaryFunction = secondaryFunction.substring(2);
                }

                // Other stuff
                String name = record.get(0);
                String addressLine1 = record.get(1);
                String addressLine2 = record.get(2);
                String city = record.get(3);
                String state = record.get(4);
                String blockLot = record.get(6);
                String primaryFunction = record.get(7);

                retailers.add(new RetailerLocation(name, addressLine1, addressLine2, city,
                        state, zipcode, blockLot, primaryFunction,
                        secondaryFunction, coords, false));

                isValidCsv = true;
            } catch (Exception e) {
                // Some error processing the line - it's either a header field or the csv is invalid.
                // If this occurs for all lines in the CSV, a CsvParserException is thrown.
            }
        }
        if (!isValidCsv) {
            throw new CsvParserException(filename);
        }
        return retailers;
    }
}
