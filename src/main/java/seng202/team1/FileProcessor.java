package seng202.team1;

import java.io.*;
import java.util.ArrayList;

/**
 * Class to handle file processing.
 * Last updated 22 August 2017.
 * @author Ollie Chick
 */
public class FileProcessor {

    /*
    * Returns an ArrayList of Strings from the filename given.
    *
    * @param filename The filename of the file to get data from.
    * @throws FileNotFoundException If the file can't be found.
    * @return ArrayList of Strings, where each string is a line from the file.
     */
    private static ArrayList<String> getDataFromFile(String filename) throws FileNotFoundException {

        ArrayList<String> data = new ArrayList<String>();
        String line;
        try {
            Reader inputFile;
            inputFile = new FileReader(filename);

            BufferedReader bufferReader = new BufferedReader(inputFile);

            while ((line = bufferReader.readLine()) != null){ //read file line by line
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

    /*
    * Takes a file named filename, which should be a csv, and processes it.
    * @todo return an object of type Entry (or whatever the class is called)
    *
    * @param filename The filename of the file to get data from.
    * @throws FileNotFoundException If the file can't be found.
     */
    public void loadCSV(String filename) throws FileNotFoundException{
        ArrayList<String> data = getDataFromFile(filename);

        for (int i = 0; i < data.size(); i++) {
            //do something
            //maybe there is an existing way to parse csvs we should implement?
        }

    }

    public void saveCSV(String filename) {


    }


    public static void main( String[] args )
    {
        String filename = "bikedata.csv";
        ArrayList<String> data = new ArrayList<String>();
        FileProcessor fp = new FileProcessor();
        try {
            data = getDataFromFile(filename);
        }
        catch(FileNotFoundException e) {
            System.out.println("Error");
        }
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
        }
    }

}
