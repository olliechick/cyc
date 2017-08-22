package seng202.team1;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Class that will pull in our CSV files and then call the appropriate constructor.
 * Most likely will be static I'm thinking. Should allow us to use it without creating an instance.
 * Last updated 22 August 2017.
 * @author Josh Burt
 * @author Ollie Chick
 */
public class CSVLoader {

    /*
    * Takes a file named filename, which should be a csv, and returns a CSVRecord.
    * This can be queried using the get(index) or get(columnName).
    *
    * @author Ollie Chick
    * @param filename The filename of the file to get data from.
    * @throws IOException If an IO error occurs.
     */
    public static void loadCSV(String filename) throws IOException {
        File csvData = new File(filename);
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
        for (CSVRecord csvRecord : parser) {
            System.out.println(csvRecord.get(0));
        }

    }


    /*
    * Main class
    * Just used for testing
    * @author Ollie Chick
     */
    public static void main(String[] args) {
        String filename = "bikedata.csv";
        try {
            loadCSV(filename);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
