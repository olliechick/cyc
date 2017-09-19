package seng202.team1;


import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * Manages the local and remote databases.
 * @author Ridge Nairn
 */
public class DatabaseManager {
    private static Connection connection;
    private static File localDatabaseFile;

    /**
     * Creates local database file, and required tables.
     *
     * @author Ridge Nairn
     */
    public static void connect() {
        String filename = "sqlite.db";
        localDatabaseFile = new File(filename);
        String url = "jdbc:sqlite:" + filename;

        if (connection == null) { // No connection yet
            try {
                DriverManager.registerDriver(new org.sqlite.JDBC());
                connection = DriverManager.getConnection(url);
                connection.setAutoCommit(false);
                if (connection != null) {
                    System.out.println("Database established and connected.");
                    createDatabaseTables();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else { // Database already exists
            System.out.println("Database already exists.");
        }
    }


    private static void createDatabaseTables() {
        // TODO: Create required tables for database
        String createTripsTable = "CREATE TABLE trip ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "duration TEXT, startTime TEXT, endTime TEXT, startLongitude TEXT NOT NULL, " +
                "startLatitude TEXT NOT NULL, endLongitude TEXT NOT NULL, endLatitude TEXT NOT NULL)";

        String createRetailerTable =  "CREATE TABLE retailer (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name TEXT NOT NULL, address TEXT NOT NULL, latitude TEXT, longitude TEXT)";

        String createWifiTable = "CREATE TABLE wifi (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "latitude TEXT, longitude TEXT, the_geom TEXT, cost TEXT, location TEXT)";

        try {
            // Check if tables already exist
            String tableQuery = "SELECT name FROM sqlite_master WHERE type='table'";
            PreparedStatement preparedStatement = connection.prepareStatement(tableQuery);
            ResultSet rs = preparedStatement.executeQuery();

            boolean tripTableExists, retailerTableExists, wifiTableExists;
            tripTableExists = retailerTableExists = wifiTableExists = false;
            while (rs.next()) {
                String table = rs.getString("name");

                switch (table) {
                    case "trip":
                        tripTableExists = true; //table already exists
                        break;
                    case "retailer":
                        retailerTableExists = true;
                        break;
                    case "wifi":
                        wifiTableExists = true;
                        break;
                }
            }
            if (wifiTableExists) {
                System.out.println("Wifi table already exists.");
            } else {
                PreparedStatement s3 = connection.prepareStatement(createWifiTable);
                s3.execute();
            }

            if (tripTableExists) {
                System.out.println("Trip table already exists.");
            } else {
                PreparedStatement s1 = connection.prepareStatement(createTripsTable);
                s1.execute();
            }

            if (retailerTableExists) {
                System.out.println("Retailer table already exists.");
            } else {
                PreparedStatement s2 = connection.prepareStatement(createRetailerTable);
                s2.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see if database is currently connected.
     *
     * @author Ridge Nairn
     * @return true if database is connected, else false.
     */
    public static boolean isDatabaseConnected() {
        return getConnection() != null;
    }

    public static Connection getConnection() {
        return connection;
    }

    /**
     * Deletes database, and all records. Primarily used for cleaning up after test cases.
     *
     * @author Ridge Nairn
     */
    public static void deleteDatabase() {
        boolean fileSuccessfullyDeleted = localDatabaseFile.delete();
        connection = null;
        if (fileSuccessfullyDeleted) {
            System.out.println("Database Deleted.");
        }
    }

    /**
     * Adds a single record to the database.
     * The type of the record is identified and is stored in its respective table.
     *
     * @author Ridge Nairn
     * @param point A point to be added to the database.
     */
    public static void addRecord(DataPoint point) throws SQLException {
        // TODO: Implement Record Adding to correct table, based on subclass
        PreparedStatement preparedStatement;
        String statement;

        if (point instanceof RetailerLocation) {
            statement = "INSERT INTO retailer (name, address) VALUES (?, ?)"; // TODO: Fix when lat/long implemented
            RetailerLocation retailer = (RetailerLocation) point;

            preparedStatement = getConnection().prepareStatement(statement);

            preparedStatement.setString(1, retailer.getName());
            preparedStatement.setString(2, retailer.getAddress());

            preparedStatement.execute();


        } else if (point instanceof WifiPoint) {
            statement = "INSERT INTO wifi (the_geom, latitude, longitude, cost, location) VALUES (?, ?, ?, ?, ?)";
            WifiPoint wifiPoint = (WifiPoint) point;

            preparedStatement = getConnection().prepareStatement(statement);

            preparedStatement.setString(1, wifiPoint.getThe_geom());
            preparedStatement.setString(2, wifiPoint.getLatitude());
            preparedStatement.setString(3, wifiPoint.getLongitude());
            preparedStatement.setString(4, wifiPoint.getCost());
            preparedStatement.setString(5, wifiPoint.getLocation());

            preparedStatement.execute();

        } else {
            System.out.println("Unexpected type.");
        }
    }

    /**
     * Adds a single bike trip record to the database.
     *
     * @author Ridge Nairn
     * @param trip An instance of BikeTrip to be added to the trip database table.
     */
    public static void addBikeTrip(BikeTrip trip) throws SQLException {
        // TODO: Don't assume values can be null
        String insert = "INSERT INTO trip (duration, startTime, endTime, startLongitude, startLatitude, " +
                "endLongitude, endLatitude) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(insert);

        statement.setString(1, trip.getTripDuration());
        statement.setString(2, trip.getStartTime());
        statement.setString(3, trip.getStopTime());
        statement.setString(4, trip.getStartLatitude());
        statement.setString(5, trip.getStartLongitude());
        statement.setString(6, trip.getEndLatitude());
        statement.setString(7, trip.getEndLongitude());
        statement.executeUpdate();

        getConnection().commit();


    }


    /**
     * Checks to see if a record point is currently stored in the database.
     * @author Ridge Nairn
     * @param point A point to be checked if it exists in the database.
     */
    public static boolean recordExists(DataPoint point) {
        return false; // TODO: Implement
    }


    public static void uploadLocalRecords() {
        // TODO: Implement upload all local records to remote database
    }

    /**
     * Fetches an ArrayList of BikeTrips from the database, of m - n size.
     *
     * @author Ridge Nairn
     * @param n lower bound to be fetched, inclusive.
     * @param m upper bound to be fetched, exclusive.
     * @return An ArrayList of BikeTrip objects of size m - n.
     */

    public static ArrayList<BikeTrip> getTrips(int n, int m) {
        String statement = "SELECT * FROM trip WHERE id >= ? AND id <= ?";
        PreparedStatement preparedStatement;
        ArrayList<BikeTrip> result = new ArrayList<>();
        try {
            preparedStatement = getConnection().prepareStatement(statement);

            preparedStatement.setInt(1, n);
            preparedStatement.setInt(2, m);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                String duration = rs.getString("duration");
                String startLatitude = rs.getString("startLatitude");
                String startLongitude = rs.getString("startLongitude");
                String endLatitude = rs.getString("endLatitude");
                String endLongitude = rs.getString("endLongitude");

                BikeTrip trip = new BikeTrip(duration, startTime, endTime, startLongitude, startLatitude, endLongitude, endLatitude);

                result.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<BikeTrip> getTrips() {
        return getTrips(0, getNumberOfBikeTrips());
    }

    /**
     * Returns the number of BikeTrip records stored in the database.
     * @author Ridge Nairn
     */
    public static int getNumberOfBikeTrips() {
        return getNumberOfRowsFromType(BikeTrip.class);
    }

    /**
     * Returns the number of records of a certain type stored in the database.
     *
     * @author Ridge Nairn
     * @param c Class of which the count is to be queried.
     */
    public static int getNumberOfRowsFromType(Class c) {
        String statement = "";
        if (c == BikeTrip.class) {
            statement = "SELECT COUNT(*) FROM trip";

        } else if (c == RetailerLocation.class) {
            statement = "SELECT COUNT(*) FROM retailer";

        } else if (c == WifiPoint.class) {
            statement = "SELECT COUNT(*) FROM wifi";

        }
        PreparedStatement preparedStatement;
        int n = -1;
        try {
            preparedStatement = getConnection().prepareStatement(statement);
            ResultSet rs = preparedStatement.executeQuery();
            n = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }


}