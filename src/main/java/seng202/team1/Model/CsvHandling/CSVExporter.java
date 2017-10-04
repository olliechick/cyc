package seng202.team1.Model.CsvHandling;

import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static seng202.team1.Model.DatabaseManager.getBikeTrips;
import static seng202.team1.Model.DatabaseManager.getRetailers;
import static seng202.team1.Model.DatabaseManager.getWifiPoints;

/**
 * Exports a list of DataPoints to a CSV file.
 * These CSV files are able to be (edited and) re-imported into the app.
 * Note that some columns will be blank - these are so that it retains backwards-compatibility
 * with the CSV files that are originally imported.
 *
 * @author Ollie Chick
 */
public class CSVExporter {

    private final static String bikeTripsHeader = "Trip duration,Start time,Stop time,,,Start point latitude,"
            + "Start point longitude,,,End point latitude,End point longitude,Bike ID,,Birth year,Gender";
    private final static String retailersHeader = "Name,Address (line 1),Address (line 2),City,State,"
            + "ZIP code,Block-Lot,Primary function,Secondary function,Latitude,Longitude";
    private final static String wifiPointsHeader = "Object ID,,,Cost,Provider,Name,Location,Latitude,"
            + "Longitude,,,Location Type,Remarks,City,SSID,Source ID,Activated,,Borough,,Neighbourhood,,"
            + "ZIP code";

    private final static String datetimeFormat = "M/d/yyyy HH:mm:ss";

    /**
     * Exports a list of Strings as lines into the file (whose name is given by filename).
     *
     * @param filename The name of the file to save
     * @param lines    A list of lines to save to the file
     * @throws IOException If an IO Exception occurs
     */
    public static void exportCSV(String filename, ArrayList<String> lines) throws IOException {
        Path file = Paths.get(filename);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }


    /**
     * Gets the bike trips from the database associated with the user denoted by username
     * and saves them to the file whose name is given by filename.
     *
     * @param filename The name of the file to save
     * @param username Username of the user whose bike trips will be exported
     * @throws IOException If an IO Exception occurs
     */
    public static void exportBikeTrips(String filename, String username) throws IOException, SQLException {
        // Import the users's bike trips from the database
        DatabaseManager.open();
        ArrayList<BikeTrip> bikeTrips = getBikeTrips(username);
        DatabaseManager.close();

        // Create an ArrayList of Strings, where each String is a bike trip line in the CSV
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(bikeTripsHeader);
        for (BikeTrip bikeTrip : bikeTrips) {
            String line;
            line = bikeTrip.getTripDuration() + ",";
            line += bikeTrip.getStartTime().format(DateTimeFormatter.ofPattern(datetimeFormat)) + ",";
            line += bikeTrip.getStopTime().format(DateTimeFormatter.ofPattern(datetimeFormat)) + ",";
            line += ",,"; //skip start station id and name
            line += bikeTrip.getStartLatitude() + ",";
            line += bikeTrip.getStartLongitude() + ",";
            line += ",,"; //skip end station id and name
            line += bikeTrip.getEndLatitude() + ",";
            line += bikeTrip.getEndLongitude() + ",";
            line += bikeTrip.getBikeId() + ",";
            line += ","; //skip usertype (Customer/Subscriber)
            line += bikeTrip.getBirthYear() + ",";
            char gender = bikeTrip.getGender();
            if (gender == 'm') {
                line += "1,";
            } else if (gender == 'f') {
                line += "2,";
            } else {
                line += "0,";
            }
            lines.add(line);
        }

        // Export the CSV into the file
        exportCSV(filename, lines);
    }


    public static void exportWifiHotspots(String filename, String username) throws IOException, SQLException {
        // Import the users's wifis from the database
        DatabaseManager.open();
        ArrayList<WifiPoint> wifiPoints = getWifiPoints(username);
        DatabaseManager.close();

        // Create an ArrayList of Strings, where each String is a bike trip line in the CSV
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(wifiPointsHeader);
        for (WifiPoint wifiPoint : wifiPoints) {
            String line;
            line = wifiPoint.getObjectId() + ",";
            line += ",,"; //skip the_geom and boro
            line += wifiPoint.getCost() + ",";
            line += wifiPoint.getProvider() + ",";
            line += wifiPoint.getName() + ",";
            line += wifiPoint.getLocation() + ",";
            line += wifiPoint.getLatitude() + ",";
            line += wifiPoint.getLongitude() + ",";
            line += ",,"; //skip x and y
            line += wifiPoint.getLocationType() + ",";
            line += wifiPoint.getRemarks() + ",";
            line += wifiPoint.getCity() + ",";
            line += wifiPoint.getSsid() + ",";
            line += wifiPoint.getSourceId() + ",";
            line += wifiPoint.getDatetimeActivated() + ",";
            line += ",,"; //skip borocode
            line += wifiPoint.getBorough() + ",";
            line += ",,"; //skip ntacode
            line += wifiPoint.getHood() + ",";
            line += ",,"; //skip coundist
            line += wifiPoint.getZipcode() + ",";
            lines.add(line);
        }

        // Export the CSV into the file
        exportCSV(filename, lines);
    }


    public static void exportRetailers(String filename, String username) throws IOException, SQLException {
        // Import the users's retailers from the database
        DatabaseManager.open();
        ArrayList<RetailerLocation> retailers = getRetailers(username);
        DatabaseManager.close();

        // Create an ArrayList of Strings, where each String is a bike trip line in the CSV
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(retailersHeader);
        for (RetailerLocation retailer : retailers) {
            String line;
            line = retailer.getName() + ",";
            line += retailer.getAddressLine1() + ",";
            line += retailer.getAddressLine2() + ",";
            line += retailer.getCity() + ",";
            line += retailer.getState() + ",";
            line += retailer.getZipcode() + ",";
            line += retailer.getBlockLot() + ",";
            line += retailer.getPrimaryFunction() + ",";
            line += retailer.getSecondaryFunction() + ",";
            line += retailer.getLatitude() + ",";
            line += retailer.getLongitude() + ",";
            line += retailer.getLongitude() + ",";
            lines.add(line);
        }

        // Export the CSV into the file
        exportCSV(filename, lines);
    }
}
