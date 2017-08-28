package seng202.team1;


import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the local and remote databases.
 * @author Ridge Nairn
 */
public class DatabaseManager {
    private static Connection connection;
    private static File localDatabaseFile;


    public static void createNewLocalDatabase() throws FileAlreadyExistsException {
        String filename = "sqlite.db";
        localDatabaseFile = new File(filename);
        String url = "jdbc:sqlite:" + filename;

        if (connection == null) { // Database does not exist
            try {
                DriverManager.registerDriver(new org.sqlite.JDBC());
                connection = DriverManager.getConnection(url);
                if (connection != null) {
                    System.out.println("Database established and connected.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else { // Database already exists
            System.out.println("Database already exists.");
            throw new FileAlreadyExistsException(filename);
        }
    }


    public static boolean isDatabaseConnected() {
        return connection != null;
    }

    public static void deleteDatabase() {
        boolean fileSuccessfullyDeleted = localDatabaseFile.delete();
        connection = null;
        if (fileSuccessfullyDeleted) {
            System.out.println("Database Deleted.");
        }
    }
}