package seng202.team1.Model;


import javax.xml.transform.Result;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     *
     * @throws SQLException could not get driver connection, or change autocommit parameter.
     */
    public static void open() throws SQLException {

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
    public static void open(boolean initialize) throws SQLException {
        if (initialize) {
            DriverManager.registerDriver(new org.sqlite.JDBC());
        }
        open();
    }


    /**
     * Closes the database connection. Called after a set of actions are performed
     * so the database is freed for use elsewhere without conflict.
     *
     * @throws SQLException Could not commit transactions, or close the connection to the database.
     */
    public static void close() throws SQLException {
        if (isDatabaseConnected()) {
            connection.commit();
            connection.close();
            connection = null;
        }
        System.out.println("Database disconnected.");
    }

    /**
     * Creates all database tables.
     */
    private static void createDatabaseTables() {
        String createTripsTable = "CREATE TABLE trip\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    listid REFERENCES list,\n" +
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
                "    listid INTEGER REFERENCES list,\n" +
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
                "    listid INTEGER REFERENCES list,\n" +
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


        String createListTable = "CREATE TABLE list\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    username TEXT NOT NULL,\n" +
                "    listName TEXT NOT NULL,\n" +
                "    type TEXT NOT NULL\n" +
                ");";

        try {
            // Check if tables already exist
            String tableQuery = "SELECT name FROM sqlite_master WHERE type='table'";
            PreparedStatement preparedStatement = connection.prepareStatement(tableQuery);
            ResultSet rs = preparedStatement.executeQuery();

            boolean tripTableExists, retailerTableExists, wifiTableExists, listTableExists;
            tripTableExists = retailerTableExists = wifiTableExists = listTableExists = false;
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
                    case "list":
                        listTableExists = true;
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
            if (listTableExists) {
                System.out.println("List table already exists.");
            } else {
                PreparedStatement s5 = connection.prepareStatement(createListTable);
                s5.execute();
                System.out.println("List table created.");
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
     * Note that it assumes the database is already open - it doesn't open it.
     *
     * @param point A point to be added to the database.
     * @throws SQLException when record cannot be added.
     * @author Ridge Nairn
     */
    private static void addRecordExistingConnection(DataPoint point, String username, String listName) throws SQLException {
        PreparedStatement preparedStatement;
        String statement;
        int numOfQs; //number of question marks to put in the statement

        if (point instanceof RetailerLocation) {
            numOfQs = 12;
            statement = "INSERT INTO retailer (listid, name, addressLine1, addressLine2, city, state, " +
                    "zipcode, blockLot, primaryFunction, secondaryFunction, latitude, longitude) " +
                    "VALUES ( " +
                    new String(new char[numOfQs - 1]).replace("\0", "?, ") + "?)";

            RetailerLocation retailer = (RetailerLocation) point;
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, getListID(username, listName, RetailerLocationList.class));
            preparedStatement.setString(2, retailer.getName());
            preparedStatement.setString(3, retailer.getAddressLine1());
            preparedStatement.setString(4, retailer.getAddressLine2());
            preparedStatement.setString(5, retailer.getCity());
            preparedStatement.setString(6, retailer.getState());
            preparedStatement.setString(7, Integer.toString(retailer.getZipcode()));
            preparedStatement.setString(8, retailer.getBlockLot());
            preparedStatement.setString(9, retailer.getPrimaryFunction());
            preparedStatement.setString(10, retailer.getSecondaryFunction());
            preparedStatement.setFloat(11, retailer.getLatitude());
            preparedStatement.setFloat(12, retailer.getLongitude());

            preparedStatement.execute();


        } else if (point instanceof WifiPoint) {
            numOfQs = 17;
            statement = "INSERT INTO wifi (listid, objectID, latitude, longitude, placeName, location, locationType, " +
                    "hood, borough, city, zipcode, cost, provider, remarks, SSID, sourceId, datetimeactivated) " +
                    "VALUES (" + new String(new char[numOfQs - 1]).replace("\0", "?, ") + "?)";

            WifiPoint wifiPoint = (WifiPoint) point;
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, getListID(username, listName, WifiPointList.class));
            preparedStatement.setInt(2, wifiPoint.getObjectId());
            preparedStatement.setFloat(3, wifiPoint.getLatitude());
            preparedStatement.setFloat(4, wifiPoint.getLongitude());
            preparedStatement.setString(5, wifiPoint.getPlaceName());
            preparedStatement.setString(6, wifiPoint.getLocation());
            preparedStatement.setString(7, wifiPoint.getLocationType());
            preparedStatement.setString(8, wifiPoint.getHood());
            preparedStatement.setString(9, wifiPoint.getBorough());
            preparedStatement.setString(10, wifiPoint.getCity());
            preparedStatement.setString(11, Integer.toString(wifiPoint.getZipcode()));
            preparedStatement.setString(12, wifiPoint.getCost());
            preparedStatement.setString(13, wifiPoint.getProvider());
            preparedStatement.setString(14, wifiPoint.getRemarks());
            preparedStatement.setString(15, wifiPoint.getSsid());
            preparedStatement.setString(16, wifiPoint.getSourceId());
            if (wifiPoint.getDatetimeActivated() == null) {
                preparedStatement.setString(17, null);
            } else {
                preparedStatement.setString(17, wifiPoint.getDatetimeActivated().toString());
            }
            preparedStatement.execute();


        } else if (point instanceof BikeTrip) {
            numOfQs = 12; //number of question marks to put in the statement

            statement = "INSERT INTO trip (listid, duration, startTime, stopTime, startLatitude, " +
                    "startLongitude, endLatitude, endLongitude, " +
                    "bikeID, gender, birthYear, tripDistance) VALUES (" +
                    new String(new char[numOfQs - 1]).replace("\0", "?, ") + "?)";

            BikeTrip trip = (BikeTrip) point;
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, getListID(username, listName, BikeTripList.class));
            preparedStatement.setLong(2, trip.getTripDuration());
            preparedStatement.setString(3, trip.getStartTime().toString());
            preparedStatement.setString(4, trip.getStopTime().toString());
            preparedStatement.setFloat(5, trip.getStartLatitude());
            preparedStatement.setFloat(6, trip.getStartLongitude());
            preparedStatement.setFloat(7, trip.getEndLatitude());
            preparedStatement.setFloat(8, trip.getEndLongitude());
            preparedStatement.setInt(9, trip.getBikeId());
            preparedStatement.setString(10, Character.toString(trip.getGender()));
            preparedStatement.setInt(11, trip.getBirthYear());
            preparedStatement.setDouble(12, trip.getTripDistance());

            preparedStatement.execute();

        } else {
            System.out.println("Unexpected type.");
        }
    }


    /**
     * Adds one record to the database.
     * This opens and closes the database, so should not be called in a loop (use addRecords() for this).
     *
     * @param point    The point to be added to the database
     * @param username The username of the user whose point is being added
     * @throws SQLException
     */
    public static void addRecord(DataPoint point, String username, String listName) throws SQLException {
        DatabaseManager.open();
        addRecordExistingConnection(point, username, listName);
        DatabaseManager.close();
    }


    /**
     * Adds a list of trips to the database.
     *
     * @param points   ArrayList of points to be added.
     * @param username Username the points are to be associated with.
     * @throws SQLException
     */
    public static void addRecords(ArrayList<? extends DataPoint> points, String username, String listName) throws SQLException {
        DatabaseManager.open();
        for (DataPoint point : points) {
            try {
                addRecordExistingConnection(point, username, listName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DatabaseManager.close();
    }


    /**
     * Gets all bike trips associated with a user.
     *
     * @param username the username of the user.
     * @return all of the bike trips associated to this user.
     */
    public static ArrayList<BikeTrip> getBikeTrips(String username, String listName) {
        String statement = "SELECT * FROM trip WHERE listid=?";
        PreparedStatement preparedStatement;
        ArrayList<BikeTrip> result = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, getListID(username, listName, BikeTripList.class));

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
     *
     * @param username the username of the user
     * @param listName the name of the list associated to the user
     * @return all of the retailer points associated to this user.
     */
    public static ArrayList<RetailerLocation> getRetailers(String username, String listName) {
        String statement = "SELECT * FROM retailer WHERE listid=?";
        PreparedStatement preparedStatement;
        ArrayList<RetailerLocation> result = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, getListID(username, listName, RetailerLocationList.class));

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
     * Gets the list id from an associated username and listName.
     * Returns an int, listID if it exists, or 0 if it does not.
     *
     * @param username Username to query
     * @param listName ListName to query
     * @param type     Type of PointList object
     * @return integer of listid as stored in database
     */
    public static int getListID(String username, String listName, Class type) {
        String statement = "SELECT id FROM list WHERE username=? AND listName=? AND type=?";


        PreparedStatement preparedStatement;
        int listid = -1;

        try {
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, listName);
            preparedStatement.setString(3, type.toGenericString());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                listid = rs.getInt(1);
                System.out.println(String.format("listid for %s of user %s is %d", listName, username, listid));
            } else {
                createNewList(username, listName, type);
                return getListIDInternally(username, listName, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listid;
    }

    private static int getListIDInternally(String username, String listName, Class type) {
        String statement = "SELECT id FROM list WHERE username=? AND listName=? AND type=?";


        PreparedStatement preparedStatement;
        int listid = -1;

        try {
            System.out.println(".......");
            System.out.println(type.toString());
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, listName);
            preparedStatement.setString(3, type.toGenericString());

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                listid = rs.getInt(1);
                System.out.println(String.format("listid for %s of user %s is %d", listName, username, listid));
            } //else {
                //throw new SQLException("Could not find existing list");
            //}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listid;
    }

    public static boolean listExists(String username, String listName, Class type) {
        String statement = "SELECT * FROM list WHERE username=? AND listName=? AND type=?";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, listName);
            preparedStatement.setString(3, type.toGenericString());

            ResultSet rs = preparedStatement.executeQuery();

            return rs.isClosed();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createNewList(String username, String listName, Class type) {

        String statement = "INSERT INTO list (username, listName, type) VALUES (?, ?, ?);";
        PreparedStatement preparedStatement;

        if (listExists(username, listName, type)) {
            try {

                preparedStatement = connection.prepareStatement(statement);

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, listName);
                preparedStatement.setString(3, type.toGenericString());

                preparedStatement.execute();

                System.out.println(String.format("List %s of user %s created", listName, username));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public static void populateList(String username, PointList pointList) {
        try {
            open();
            if (pointList instanceof RetailerLocationList) {
                for (RetailerLocation retailerLocation : ((RetailerLocationList) pointList).getRetailerLocations()) {
                    addRecordExistingConnection(retailerLocation, username, pointList.getListName());
                }

            } else if (pointList instanceof WifiPointList) {
                for (WifiPoint wifiPoint : ((WifiPointList) pointList).getWifiPoints()) {
                    addRecordExistingConnection(wifiPoint, username, pointList.getListName());
                }
            } else if (pointList instanceof BikeTripList) {
                for (BikeTrip bikeTrip : ((BikeTripList) pointList).getBikeTrips()) {
                    addRecordExistingConnection(bikeTrip, username, pointList.getListName());
                }
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the lists associated with a user of a certain type.
     *
     * @param username Username of the user in question
     * @param type     Type of list, i.e. BikeTripList.class
     */
    public static ArrayList<String> getLists(String username, Class type) {
        ArrayList<String> result = new ArrayList<>();
        PreparedStatement preparedStatement;

        String statement = "SELECT listName FROM list WHERE username=? AND type=?";

        try {
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, type.toGenericString());

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Gets all of the wifi points associated with a user.
     *
     * @param username the username of the user.
     * @return all of WiFi points associated to this user.
     */
    public static ArrayList<WifiPoint> getWifiPoints(String username, String listName) {
        String statement = "SELECT * FROM wifi WHERE listid=?";
        PreparedStatement preparedStatement;
        ArrayList<WifiPoint> result = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, getListID(username, listName, WifiPointList.class));

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
                LocalDateTime dateTime;
                if (dateTimeActivated == null) {
                    dateTime = null;
                } else {
                    dateTime = LocalDateTime.parse(dateTimeActivated);
                }

                WifiPoint wifiPoint = new WifiPoint(objectID, coords, placeName, location, locationType, hood,
                        borough, city, zipcode, cost, provider, remarks, ssid, sourceID, dateTime);

                result.add(wifiPoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}