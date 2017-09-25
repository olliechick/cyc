package seng202.team1.Model.CsvHandling;

/**
 * Exception class to handle when a CSV can't be parsed (e.g. as bike trips).
 *
 * @author Ollie Chick
 */
public class CsvParserException extends Exception {

    private String filename;

    /**
     * Constructor for CsvParserException
     *
     * @param filename The name of the file that couldn't be parsed.
     */
    public CsvParserException(String filename) {
        this.filename = filename;
    }


    public String getFilename() {
        return filename;
    }

}
