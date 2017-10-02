package seng202.team1.Model.CsvHandling;

import seng202.team1.Model.BikeTrip;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Exports a list of DataPoints to a CSV file.
 * These CSV files are able to be (edited and) re-imported into the app.
 * Note that some columns will be blank - these are
 *
 * @author Ollie Chick
 */
public class CSVExporter {

    private final static String bikeTripsHeader = "Trip duration,Start time,Stop time,,,Start point latitude,"
            + "Start point longitude,,,End point latitude,End point longitude,Bike ID,,Birth year,Gender";
    public final static String datetimeFormat = "M/d/yyyy HH:mm:ss";

    /**
     * Exports a list of Strings as lines into the file (whose name is given by filename).
     *
     * @param filename The name of the file to save
     * @param lines    A list of lines to save to the file
     * @throws IOException If an IO Exception occurs
     */
    public static void exportCSV(String filename, List<String> lines) throws IOException {
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
    public static void exportBikeTrips(String filename, String username) throws IOException {
        ArrayList<BikeTrip> bikeTrips = new ArrayList<>();
        //bikeTrips = getUserTrips(username);
        try {
            bikeTrips = CSVLoader.populateBikeTrips();
        } catch (CsvParserException e) {
            System.out.println("Error: " + e.getMessage());
        }

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
        exportCSV(filename, lines);
    }


    public static void main(String[] args) {
        try {
            exportBikeTrips("myBikeTrips.csv", "a");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
