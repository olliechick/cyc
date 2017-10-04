package seng202.team1.Model;


import java.awt.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Manages the local and remote databases.
 *
 * @author Ridge Nairn
 * @author Ollie Chick
 */
public class DatabaseManager {
    private static Connection connection;
    private static File localDatabaseFile;


    /**
     * Opens the database connection, creating it if it does not exist. Does not register JDBC driver.
     * @throws SQLException could not get driver connection, or change autocommit parameter.
     */
    public static void open() throws SQLException{

        String filename = "sqlite.db";
        localDatabaseFile = new File(Directory.DATABASES.directory() + filename);

        boolean databaseExists = localDatabaseFile.exists();
        String url = "jdbc:sqlite:" + localDatabaseFile.getAbsolutePath();

        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(false);
        if (databaseExists) {
            System.out.println("Database connected.");
        } else {
            createDatabaseTables();
            System.out.println("Database established and connected.");
        }

    }


    /**
     * Opens the database connection, creating it if it does not exist.
     *
     * @param initialize registers the JDBC driver, for initial loading on some machines.
     * @throws SQLException could not register driver, or could not open database.
     */
    public static void open(boolean initialize) throws SQLException{
        if (initialize) {
            DriverManager.registerDriver(new org.sqlite.JDBC());
        }
        open();
    }


    /**
     * Closes the database connection. Called after a set of actions are performed
     * so the database is freed for use elsewhere without conflict.
     * @throws SQLException Could not commit transactions, or close the connection to the database.
     */
    public static void close() throws SQLException{
        if (isDatabaseConnected()) {
            connection.commit();
            connection.close();
        }
    }

    /**
     * Creates all database tables.
     */
    private static void createDatabaseTables() {
        // TODO: Create required tables for database
        String createTripsTable = "CREATE TABLE trip\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    username TEXT,\n" +
                "    duration BIGINT,\n" +
                "    startTime TEXT,\n" +
                "    stopTime TEXT,\n" +
                "    startLatitude FLOAT,\n" +
                "    startLongitude FLOAT,\n" +
                "    endLatitude FLOAT,\n" +
                "    endLongitude FLOAT,\n" +
                "    bikeID INTEGER,\n" +
                "    gender VARCHAR(1),\n" +
                "    birthYear INTEGER,\n" +
                "    tripDistance DOUBLE\n" +
                ");";

        String createRetailerTable = "CREATE TABLE retailer\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    username TEXT,\n" +
                "    name TEXT NOT NULL,\n" +
                "    addressLine1 TEXT,\n" +
                "    addressLine2 TEXT,\n" +
                "    city TEXT,\n" +
                "    state TEXT,\n" +
                "    zipcode INTEGER,\n" +
                "    blockLot TEXT,\n" +
                "    primaryFunction TEXT,\n" +
                "    secondaryFunction TEXT,\n" +
                "    latitude FLOAT,\n" +
                "    longitude FLOAT\n" +
                ");";

        String createWifiTable = "CREATE TABLE wifi\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    username TEXT,\n" +
                "    objectID INTEGER,\n" +
                "    latitude FLOAT,\n" +
                "    longitude FLOAT,\n" +
                "    placeName TEXT,\n" +
                "    location TEXT,\n" +
                "    locationType TEXT,\n" +
                "    hood TEXT,\n" +
                "    borough TEXT,\n" +
                "    city TEXT,\n" +
                "    zipcode INTEGER,\n" +
                "    cost TEXT,\n" +
                "    provider TEXT,\n" +
                "    remarks TEXT,\n" +
                "    SSID TEXT,\n" +
                "    sourceId TEXT,\n" +
                "    dateTimeActivated TEXT\n" +
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
                PreparedStatement s1 = connection.prepareStatement(createWifiTable);
                s1.execute();
                System.out.println("Wifi table created.");
            }

            if (tripTableExists) {
                System.out.println("Trip table already exists.");
            } else {
                PreparedStatement s2 = connection.prepareStatement(createTripsTable);
                s2.execute();
                System.out.println("Trip table created.");
            }

            if (retailerTableExists) {
                System.out.println("Retailer table already exists.");
            } else {
                PreparedStatement s3 = connection.prepareStatement(createRetailerTable);
                s3.execute();
                System.out.println("Retailer table created.");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks to see if the database is currently connected.
     *
     * @return true if the database is connected.
     * @author Ridge Nairn
     */
    public static boolean isDatabaseConnected() {
        return connection != null;
    }


    /**
     * Deletes database, and all records. Primarily used for cleaning up after test cases.
     *
     * @author Ridge Nairn
     */
    public static void deleteDatabase() {
        boolean fileSuccessfullyDeleted = localDatabaseFile.delete();
        File f = new File(Directory.DATABASES.directory() + "sqlite.db-journal");
        boolean journalSuccessfullyDeleted = f.delete();
        connection = null;
        if (fileSuccessfullyDeleted) {
            System.out.println("Database Deleted.");
        }
        if (journalSuccessfullyDeleted) {
            System.out.println("Journal files successfully deleted.");
        }
    }


    /**
     * Adds a single record to the database.
     * The type of the record is identified and is stored in its respective table.
     *
     * @param point A point to be added to the database.
     * @throws SQLException when record cannot be added.
     * @author Ridge Nairn
     */
    public static void addRecord(DataPoint point, String username) throws SQLException {
        PreparedStatement preparedStatement;
        String statement;
        int numOfQs; //number of question marks to put in the statement


        if (point instanceof RetailerLocation) {
            numOfQs = 12;
            statement = "INSERT INTO retailer (name, addressLine1, addressLine2, city, state, zipcode, blockLot, " +
                    "primaryFunction, secondaryFunction, latitude, longitude, username) " +
                    "VALUES (" + new String(new char[numOfQs - 1]).replace("\0", "?, ") + "?)";
            RetailerLocation retailer = (RetailerLocation) point;

            preparedStatement = connection.prepareStatement(statement);

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
            preparedStatement.setString(12, username);

            preparedStatement.execute();

        } else if (point instanceof WifiPoint) {
            numOfQs = 17;
            statement = "INSERT INTO wifi (objectID, latitude, longitude, placeName, location, locationType, " +
                    "hood, borough, city, zipcode, cost, provider, remarks, SSID, sourceId, datetimeactivated, username) " +
                    "VALUES (" + new String(new char[numOfQs - 1]).replace("\0", "?, ") + "?)";
            WifiPoint wifiPoint = (WifiPoint) point;

            preparedStatement = connection.prepareStatement(statement);

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
            preparedStatement.setString(17, username);

            preparedStatement.execute();


        } else {
            System.out.println("Unexpected type.");
        }
    }


    /**
     * Adds a single bike trip record to the database.
     *
     * @param trip An instance of BikeTrip to be added to the trip database table.
     * @param username The username of the account the trip is tied to.
     * @throws SQLException when the row could not be inserted
     * @author Ridge Nairn
     * @author Ollie Chick
     */
    public static void addBikeTrip(BikeTrip trip, String username) throws SQLException {
        int numOfQs = 12; //number of question marks to put in the statement

        String insert = "INSERT INTO trip (duration, startTime, stopTime, startLatitude, " +
                "startLongitude, endLatitude, endLongitude, bikeID, gender, birthYear, " +
                "tripDistance, username) VALUES (" +

                new String(new char[numOfQs - 1]).replace("\0", "?, ") + "?)";

        PreparedStatement statement = connection.prepareStatement(insert);

        statement.setLong(1, trip.getTripDuration());
        statement.setString(2, trip.getStartTime().toString());
        statement.setString(3, trip.getStopTime().toString());
        statement.setFloat(4, trip.getStartLatitude());
        statement.setFloat(5, trip.getStartLongitude());
        statement.setFloat(6, trip.getEndLatitude());
        statement.setFloat(7, trip.getEndLongitude());
        statement.setInt(8, trip.getBikeId());
        statement.setString(9, Character.toString(trip.getGender()));
        statement.setInt(10, trip.getBirthYear());
        statement.setDouble(11, trip.getTripDistance());
        statement.setString(12, username);

        statement.execute();

    }


    /**
     * Adds a list of trips to the database.
     * @param trips ArrayList of trips to be added.
     * @param username Username the trips are to be associated with.
     */
    public static void addBikeTrips(ArrayList<BikeTrip> trips, String username) {
        for (BikeTrip trip: trips) {
            try {
                addBikeTrip(trip, username);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Gets all bike trips associated with a user.
     * @param username the username of the user.
     * @return all of the bike trips associated to this user.
     */
    public static ArrayList<BikeTrip> getBikeTrips(String username) {
        String statement = "SELECT * FROM trip WHERE username=?";
        PreparedStatement preparedStatement;
        ArrayList<BikeTrip> result = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String startTimeString = rs.getString("startTime");
                String stopTimeString = rs.getString("stopTime");
                Long duration = rs.getLong("duration");
                Float startLatitude = rs.getFloat("startLatitude");
                Float startLongitude = rs.getFloat("startLongitude");
                Float endLatitude = rs.getFloat("endLatitude");
                Float endLongitude = rs.getFloat("endLongitude");
                int bikeID = rs.getInt("bikeID");
                char gender = rs.getString("gender").toCharArray()[0];
                int birthYear = rs.getInt("birthYear");
                double tripDistance = rs.getDouble("tripDistance");

                LocalDateTime startTime = LocalDateTime.parse(startTimeString);
                LocalDateTime stopTime = LocalDateTime.parse(stopTimeString);

                Point.Float startPoint = new Point.Float(startLongitude, startLatitude);
                Point.Float stopPoint = new Point.Float(endLongitude, endLatitude);

                BikeTrip trip = new BikeTrip(duration, startTime, stopTime, startPoint, stopPoint,
                        bikeID, gender, birthYear, tripDistance);

                result.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Gets all retailers associated with a user.
     * @param username the username of the user
     * @return all of the retailer points associated to this user.
     */
    public static ArrayList<RetailerLocation> getRetailers(String username) {
        String statement = "SELECT * FROM retailer WHERE username=?";
        PreparedStatement preparedStatement;
        ArrayList<RetailerLocation> result = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String addressLine1 = rs.getString("addressLine1");
                String addressLine2 = rs.getString("addressLine2");
                String city = rs.getString("city");
                String state = rs.getString("state");
                int zipcode = rs.getInt("zipcode");
                String blocklot = rs.getString("blocklot");
                String primaryFunction = rs.getString("primaryFunction");
                String secondaryFunction = rs.getString("secondaryFunction");
                Float latitude = rs.getFloat("latitude");
                Float longitude = rs.getFloat("longitude");

                Point.Float location = new Point.Float(longitude, latitude);

                RetailerLocation retailerLocation = new RetailerLocation(name, addressLine1, addressLine2, city, state, zipcode, blocklot, primaryFunction, secondaryFunction, location);

                result.add(retailerLocation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Gets all of the wifi points associated with a user.
     * @param username the username of the user.
     * @return all of WiFi points associated to this user.
     */
    public static ArrayList<WifiPoint> getWifiPoints(String username) {
        String statement = "SELECT * FROM wifi WHERE username=?";
        PreparedStatement preparedStatement;
        ArrayList<WifiPoint> result = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int objectID = rs.getInt("objectID");
                Float latitude = rs.getFloat("latitude");
                Float longitude = rs.getFloat("longitude");
                String placeName = rs.getString("placeName");
                String location = rs.getString("location");
                String locationType = rs.getString("locationType");
                String hood = rs.getString("hood");
                String borough = rs.getString("borough");
                String city = rs.getString("city");
                int zipcode = rs.getInt("zipcode");
                String cost = rs.getString("cost");
                String provider = rs.getString("provider");
                String remarks = rs.getString("remarks");
                String ssid = rs.getString("SSID");
                String sourceID = rs.getString("sourceId");
                String dateTimeActivated = rs.getString("dateTimeActivated");

                Point.Float coords = new Point.Float(longitude, latitude);
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeActivated);

                WifiPoint wifiPoint = new WifiPoint(objectID, coords, placeName, location, locationType, hood,
                        borough, city, zipcode, cost, provider, remarks, ssid, sourceID, dateTime);

                result.add(wifiPoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Returns the number of records of a certain type stored in the database,
     * that are associated with a user.
     *
     * @param c Class of which the count is to be queried.
     * @param username Username of the user.
     * @return number of records to type c in database
     * @author Ridge Nairn
     */
    public static int getNumberOfRowsFromType(Class c, String username) {
        String statement = "";
        if (c == BikeTrip.class) {
            statement = "SELECT COUNT(*) FROM trip WHERE username=?";

        } else if (c == RetailerLocation.class) {
            statement = "SELECT COUNT(*) FROM retailer WHERE username=?";

        } else if (c == WifiPoint.class) {
            statement = "SELECT COUNT(*) FROM wifi WHERE username=?";

        }
        PreparedStatement preparedStatement;
        int n = -1;
        try {
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            n = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }
}