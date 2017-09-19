package seng202.team1;


import java.awt.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
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
        String createTripsTable = "CREATE TABLE trip\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    duration BIGINT,\n" +
                "    startTime TEXT,\n" +
                "    stopTime TEXT,\n" +
                "    startx FLOAT,\n" +
                "    starty FLOAT,\n" +
                "    endx FLOAT,\n" +
                "    endy FLOAT,\n" +
                "    bikeID INTEGER,\n" +
                "    gender VARCHAR(1),\n" +
                "    birthYear INTEGER,\n" +
                "    tripDistance DOUBLE,\n" +
                "    googleData BLOB,\n" +
                "    isUserDefined BOOLEAN\n" +
                ");";

        String createRetailerTable =  "CREATE TABLE retailer\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    addressLine1 TEXT NOT NULL,\n" +
                "    addressLine2 TEXT,\n" +
                "    city TEXT,\n" +
                "    state TEXT,\n" +
                "    zipcode VARCHAR(5),\n" +
                "    blockLot TEXT,\n" +
                "    primaryFunction TEXT,\n" +
                "    secondaryFunction TEXT,\n" +
                "    latitude FLOAT,\n" +
                "    longitude FLOAT\n" +
                ");";

        String createWifiTable = "CREATE TABLE wifi\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    objectID INTEGER,\n" +
                "    locationx FLOAT,\n" +
                "    locationy FLOAT,\n" +
                "    placeName TEXT,\n" +
                "    location TEXT,\n" +
                "    locationType TEXT,\n" +
                "    hood TEXT,\n" +
                "    borough TEXT,\n" +
                "    city TEXT,\n" +
                "    zipcode VARCHAR(5),\n" +
                "    cost TEXT,\n" +
                "    provider TEXT,\n" +
                "    remarks TEXT,\n" +
                "    SSID TEXT,\n" +
                "    sourceId TEXT,\n" +
                "    datetimeactivated TEXT\n" +
                ");";

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
            statement = "INSERT INTO retailer (name, addressLine1, addressLine2, city, state, zipcode, blockLot, " +
                    "primaryFunction, secondaryFunction, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            RetailerLocation retailer = (RetailerLocation) point;

            preparedStatement = getConnection().prepareStatement(statement);

            preparedStatement.setString(1, retailer.getName());
            preparedStatement.setString(2, retailer.getAddressLine1());
            preparedStatement.setString(3, retailer.getAddressLine2());
            preparedStatement.setString(4, retailer.getCity());
            preparedStatement.setString(5, retailer.getState());

            preparedStatement.setString(6, Integer.toString(retailer.getZipcode()));
            preparedStatement.setString(7, retailer.getBlockLot());
            preparedStatement.setString(8, retailer.getPrimaryFunction());
            preparedStatement.setString(9, retailer.getSecondaryFunction());
            preparedStatement.setFloat(10, retailer.getLatitude());
            preparedStatement.setFloat(11, retailer.getLongitude());

            preparedStatement.execute();


        } else if (point instanceof WifiPoint) {
            statement = "INSERT INTO wifi (objectID, locationx, locationy, placeName, location, locationType, " +
                    "hood, borough, city, zipcode, cost, provider, remarks, SSID, sourceId, datetimeactivated) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            WifiPoint wifiPoint = (WifiPoint) point;

            preparedStatement = getConnection().prepareStatement(statement);

            preparedStatement.setInt(1, wifiPoint.getObjectId());
            preparedStatement.setFloat(2, wifiPoint.getLatitude());
            preparedStatement.setFloat(3, wifiPoint.getLongitude());
            preparedStatement.setString(4, wifiPoint.getPlaceName());
            preparedStatement.setString(5, wifiPoint.getLocation());

            preparedStatement.setString(6, wifiPoint.getLocationType());
            preparedStatement.setString(7, wifiPoint.getHood());
            preparedStatement.setString(8, wifiPoint.getBorough());
            preparedStatement.setString(9, wifiPoint.getCity());
            preparedStatement.setString(10, Integer.toString(wifiPoint.getZipcode()));

            preparedStatement.setString(11, wifiPoint.getCost());
            preparedStatement.setString(12, wifiPoint.getProvider());
            preparedStatement.setString(13, wifiPoint.getRemarks());
            preparedStatement.setString(14, wifiPoint.getSsid());
            preparedStatement.setString(15, wifiPoint.getSourceId());
            preparedStatement.setString(16, wifiPoint.getDatetimeActivated().toString());

            preparedStatement.addBatch();

            preparedStatement.executeBatch();

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
        String insert = "INSERT INTO trip (duration, startTime, stopTime, startx, starty, endx, endy, bikeID, gender, " +
                "birthYear, tripDistance, googleData) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(insert);

        statement.setLong(1, trip.getTripDuration());
        statement.setString(2, trip.getStartTime().toString());
        statement.setString(3, trip.getStopTime().toString());
        statement.setFloat(4, trip.getStartLatitude());
        statement.setFloat(5, trip.getStartLongitude());
        statement.setFloat(6, trip.getEndLatitude());
        statement.setFloat(7, trip.getEndLongitude());
        statement.setInt(8, trip.getBikeID());
        statement.setInt(9, trip.getGender());
        statement.setInt(10, trip.getBirthYear());
        statement.setDouble(11, trip.getTripDistance());
        statement.setString(12, trip.getGoogleData());
        // statement.setBoolean(13, trip.isUserDefined());
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
                String startTimeString = rs.getString("startTime");
                String stopTimeString = rs.getString("stopTime");
                Long duration = rs.getLong("duration");
                Float startx = rs.getFloat("startx");
                Float starty = rs.getFloat("starty");
                Float endx = rs.getFloat("endx");
                Float endy = rs.getFloat("endy");
                int bikeID = rs.getInt("bikeID");
                char gender = rs.getString("gender").toCharArray()[0];
                int birthYear = rs.getInt("birthYear");
                // double tripDistance = rs.getDouble("tripDistance");          // Currently not required in constructor
                // String googleData = rs.getBlob("googleData").toString();     // Also not used
                boolean isUserDefined = rs.getBoolean("isUserDefined");
                
                LocalDateTime startTime = LocalDateTime.parse(startTimeString);
                LocalDateTime stopTime = LocalDateTime.parse(stopTimeString);

                Point.Float startPoint = new Point.Float(startx, starty);
                Point.Float stopPoint = new Point.Float(endx, endy);
                

                BikeTrip trip = new BikeTrip(duration, startTime, stopTime, startPoint, stopPoint, bikeID, gender, birthYear, isUserDefined);

                result.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<BikeTrip> getAllTrips() {
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