package seng202.team1;

import java.io.*;
import java.util.ArrayList;

/**
 * Class to handle file processing.
 * Last updated 22 August 2017.
 * @author Ollie Chick
 */
public class FileProcessor {

    public ArrayList<String> loadCSV(String filename) throws FileNotFoundException {

        ArrayList<String> data = new ArrayList<String>();
        String line;
        try {
            Reader inputFile;
            inputFile = new FileReader(filename);

            BufferedReader bufferReader = new BufferedReader(inputFile);

            while ((line = bufferReader.readLine()) != null) {
                data.add(line);
            }

            bufferReader.close(); //tidy up after reading file
            inputFile.close();
        } catch (IOException e) {
            //If there is an IO error here just give up
            System.err.println("Error while reading file line by line: " + e.getMessage());
            System.exit(0);
        }

        return data;

    }

    public void saveCSV(String filename) {


    }


    public static void main( String[] args )
    {
        String filename = "bikedata.csv";
        ArrayList<String> data = new ArrayList<String>();
        FileProcessor fp = new FileProcessor();
        try {
            data = fp.loadCSV(filename);
        }
        catch(FileNotFoundException e) {
            System.out.println("Error");
        }
        for (int i = 0; i < data.size(); i++)
        System.out.println(data.get(i));
    }

}
