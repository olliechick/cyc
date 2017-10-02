package seng202.team1.Model.CsvHandling;

import seng202.team1.Model.BikeTrip;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exports a list of DataPoints to a CSV file.
 *
 * @author Ollie Chick
 */
public class CSVExporter {

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
     * Gets the bike trips from the database and saves them to the file whose name is given by filename.
     *
     * @param filename The name of the file to save
     */
    public static void exportBikeTrips(String filename) throws IOException{
        //TODO get bike trips from database
        List<String> lines = Arrays.asList("BT1", "BT2");
        exportCSV(filename, lines);
    }


    public static void main(String[] args) {
        try {
            exportBikeTrips("myBikeTrips.csv");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
