package seng202.team1;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
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

    /**
    * Takes a file named filename, which should be a csv, and returns an Arraylist of type CSVRecord.
    * This can be queried using the get(index) or get(columnName) [haven't figured out columnName though]
    *
    * @author Ollie Chick
    * @author Josh Burt
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
     * Calls the load CSV method and populates an array list with a set of BikeTrip objects from biketrips.csv.
     * If the file does not exist, then the error message is printed.
     * @author Ollie Chick
     * @return ArrayList<DataPoint>
     */
    public static ArrayList<DataPoint> populateBikeTrips() {
        String filename = "src/main/resources/csv/biketrips.csv";
        return populateBikeTrips(filename);
    }

    /**
     * Calls the load CSV method and populates an array list with a set of BikeTrip objects from a given filename.
     * If the filename does not exist, then the error message is printed.
     * @param filename name of the file the data is to be loaded from.
     * @author Josh Burt
     * @author Ollie Chick
     * @return ArrayList<DataPoint>
     */
    public static ArrayList<DataPoint> populateBikeTrips(String filename){ //
        ArrayList<DataPoint> trips = new ArrayList<DataPoint>();
        try {
            ArrayList<CSVRecord> tripData = loadCSV(filename);
            for (CSVRecord record : tripData){
                long tripDuration = new Long(record.get(0));
                LocalDateTime startTime = LocalDateTime.parse(record.get(1), DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss"));
                LocalDateTime stopTime = LocalDateTime.parse(record.get(2), DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss"));
                Point.Float startPoint = new Point.Float(Float.parseFloat(record.get(5)), Float.parseFloat(record.get(6)));
                Point.Float endPoint = new Point.Float(Float.parseFloat(record.get(9)), Float.parseFloat(record.get(10)));
                int bikeID = Integer.parseInt(record.get(11));
                char gender;
                if (record.get(14).equals("1")) {
                    gender = 'm';
                } else if (record.get(14).equals("2")) {
                    gender = 'f';
                } else {
                    gender = 'u';
                }
                int birthyear = Integer.parseInt(record.get(13));

                trips.add(new BikeTrip(tripDuration, startTime, stopTime, startPoint, endPoint, bikeID, gender, birthyear));

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return trips;
    }


    /**
     * Calls the load CSV method and populates an array list with a set of WifiPoint objects
     * @param filename name of the file the data is to be loaded from.
     * @author Josh Burt
     * @return ArrayList<DataPoint>
     */
    public static ArrayList<DataPoint> populateWifiHotspots(String filename){
        ArrayList<DataPoint> wifiSpots = new ArrayList<DataPoint>();
        try {
            ArrayList<CSVRecord> wifiData = loadCSV(filename);
            for(CSVRecord record : wifiData){
                String objectId = record.get(0);
                String the_geom = record.get(1);
                String borough = record.get(2);
                String cost = record.get(3);
                String location = record.get(6);
                String latitude = record.get(7);
                String longitude = record.get(8);
                wifiSpots.add(new WifiPoint(objectId, the_geom, borough, cost, latitude, longitude, location));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return wifiSpots;
    }


    /**
     * Calls the load CSV method and populates an array list with a set of RetailerLocation objects
     * @param filename name of the file the data is to be loaded from.
     * @author Ollie Chick
     * @return ArrayList<DataPoint>
     */
    public static ArrayList<DataPoint> populateRetailers(String filename){
        ArrayList<DataPoint> retailers = new ArrayList<DataPoint>(); //array list of retailers
        try {
            ArrayList<CSVRecord> retailerData = loadCSV(filename);
            for(CSVRecord record : retailerData){
                String name = record.get(0);
                String street_address = record.get(1);
                String address_line2 = record.get(2);
                String city = record.get(3);
                String state = record.get(4);
                String zipcode = record.get(5);
                String address;
                if (address_line2.isEmpty()) {
                    address = street_address + ", " + city + ", " + state + " " + zipcode;
                } else {
                    address = street_address + ", " + address_line2 + ", " + city + ", " + state + " " + zipcode;
                }
                String primaryFunction = record.get(7);
                String secondaryFunction = record.get(8);
                if (secondaryFunction.length() > 2) {
                    secondaryFunction = secondaryFunction.substring(2);
                }
                retailers.add(new RetailerLocation(name, address, primaryFunction, secondaryFunction));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return retailers;
    }

    /**
    * Main class
    * Just used for testing
    * @author Ollie Chick
     */
    public static void main(String[] args) {
        String filename = "wifiTester.csv";
        //populateBikeTrips(filename);

        ArrayList<DataPoint> trips = populateWifiHotspots(filename);//populateBikeTrips(filename);
        for (DataPoint trip :trips) {
            System.out.println(trip);
        }

        ArrayList<DataPoint> retailers = populateRetailers("ret.csv");//populateBikeTrips(filename);
        for (DataPoint retailer : retailers) {
            System.out.println(retailer);
        }

    }

}
