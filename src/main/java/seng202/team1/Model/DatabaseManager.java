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
    private static boolean inUse = false;


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
        if (inUse()) {
            try {
                System.out.println("DB in use - sleeping for 1s");
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            inUse = true;

            if (databaseExists) {
                System.out.println("Database connected.");
            } else {
                createDatabaseTables();
                System.out.println("Database established and connected.");
            }
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
            inUse = false;
        }
        System.out.println("Database disconnected.");
    }

    public static boolean inUse() { return inUse; };

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
     * Populates the database with default data from .csv files.
     * @param username username of the user to which the data should be associated to
     */
    public static void populateDatabaseWithDefaultValues(String username) {

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
            int listid = getListID(username, listName, RetailerLocationList.class);

            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, listid);
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
            int listid = getListID(username, listName, WifiPointList.class);

            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, listid);
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
            int listid = getListID(username, listName, BikeTripList.class);

            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, listid);
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
        addRecordExistingConnection(point, username, listName);
    }


    /**
     * Adds a list of trips to the database.
     *
     * @param points   ArrayList of points to be added.
     * @param username Username the points are to be associated with.
     * @throws SQLException
     */
    public static void addRecords(ArrayList<? extends DataPoint> points, String username, String listName) throws SQLException {
        for (DataPoint point : points) {
            try {
                addRecordExistingConnection(point, username, listName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    /**
     * Deletes a list from the database.
     * @param username User whose list is being deleted.
     * @param listName Name of the list that is being deleted.
     * @param type Type of list that is being deleted, e.g. BikeTripList.class
     */
    public static void deleteList(String username, String listName, Class type) {
        String statement_template = "DELETE FROM %s WHERE listid=?;";
        String statement_2 = "DELETE FROM list WHERE listName=? AND type=? AND username=?";
        PreparedStatement preparedStatement, preparedStatement2;

        try {
            int listid = getListID(username, listName, type);
            String table = null;

            if (type == BikeTripList.class) {
                table = "trip";
            } else if (type == WifiPointList.class) {
                table = "wifi";
            } else if (type == RetailerLocationList.class) {
                table = "retailer";
            }

            String statement = String.format(statement_template, table);

            // Delete points
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, listid);
            preparedStatement.execute();

            // Delete list
            preparedStatement2 = connection.prepareStatement(statement_2);
            preparedStatement2.setString(1, listName);
            preparedStatement2.setString(2, type.toGenericString());
            preparedStatement2.setString(3, username);
            preparedStatement2.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a point with new values
     * @param username Username of the owner of the point
     * @param listName Name of the list the point exists in
     * @param oldPoint Point to be updated
     * @param newPoint New values to be included in database
     */
    public static void updatePoint(String username, String listName, DataPoint oldPoint, DataPoint newPoint) {
        PreparedStatement preparedStatement1;
        PreparedStatement preparedStatement2;

        String statement1;
        String statement2;

        int idToUpdate;


        if (oldPoint instanceof WifiPoint) {
            statement1 = "SELECT id FROM wifi WHERE listid=? AND objectID=? AND latitude=? AND longitude=? AND placeName=? AND location=? AND locationType=? AND " +
                    "hood=? AND borough=? AND city=? AND zipcode=? AND cost=? AND provider=? AND remarks=? AND SSID=? AND sourceId=? AND datetimeactivated=?";
            try {
                int listid = getListID(username, listName, WifiPointList.class);

                WifiPoint wifiPoint = (WifiPoint) oldPoint; 
                preparedStatement1 = connection.prepareStatement(statement1);

                preparedStatement1.setInt(1, listid);
                preparedStatement1.setInt(2, wifiPoint.getObjectId());
                preparedStatement1.setFloat(3, wifiPoint.getLatitude());
                preparedStatement1.setFloat(4, wifiPoint.getLongitude());
                preparedStatement1.setString(5, wifiPoint.getPlaceName());
                preparedStatement1.setString(6, wifiPoint.getLocation());
                preparedStatement1.setString(7, wifiPoint.getLocationType());
                preparedStatement1.setString(8, wifiPoint.getHood());
                preparedStatement1.setString(9, wifiPoint.getBorough());
                preparedStatement1.setString(10, wifiPoint.getCity());
                preparedStatement1.setString(11, Integer.toString(wifiPoint.getZipcode()));
                preparedStatement1.setString(12, wifiPoint.getCost());
                preparedStatement1.setString(13, wifiPoint.getProvider());
                preparedStatement1.setString(14, wifiPoint.getRemarks());
                preparedStatement1.setString(15, wifiPoint.getSsid());
                preparedStatement1.setString(16, wifiPoint.getSourceId());
                if (wifiPoint.getDatetimeActivated() == null) {
                    preparedStatement1.setString(17, null);
                } else {
                    preparedStatement1.setString(17, wifiPoint.getDatetimeActivated().toString());
                }


                ResultSet rs = preparedStatement1.executeQuery();

                if (rs.next()) {
                    System.out.println("NEXT");
                    idToUpdate = rs.getInt(1);

                    statement2 = "UPDATE wifi SET listid=?, objectID=?, latitude=?, longitude=?, placeName=?, location=?, locationType=?, " +
                            "hood=?, borough=?, city=?, zipcode=?, cost=?, provider=?, remarks=?, SSID=?, sourceId=?, datetimeactivated=? WHERE id=?";

                    preparedStatement2 = connection.prepareStatement(statement2);

                    wifiPoint = (WifiPoint) newPoint;

                    preparedStatement2.setInt(1, listid);
                    preparedStatement2.setInt(2, wifiPoint.getObjectId());
                    preparedStatement2.setFloat(3, wifiPoint.getLatitude());
                    preparedStatement2.setFloat(4, wifiPoint.getLongitude());
                    preparedStatement2.setString(5, wifiPoint.getPlaceName());
                    preparedStatement2.setString(6, wifiPoint.getLocation());
                    preparedStatement2.setString(7, wifiPoint.getLocationType());
                    preparedStatement2.setString(8, wifiPoint.getHood());
                    preparedStatement2.setString(9, wifiPoint.getBorough());
                    preparedStatement2.setString(10, wifiPoint.getCity());
                    preparedStatement2.setString(11, Integer.toString(wifiPoint.getZipcode()));
                    preparedStatement2.setString(12, wifiPoint.getCost());
                    preparedStatement2.setString(13, wifiPoint.getProvider());
                    preparedStatement2.setString(14, wifiPoint.getRemarks());
                    preparedStatement2.setString(15, wifiPoint.getSsid());
                    preparedStatement2.setString(16, wifiPoint.getSourceId());
                    if (wifiPoint.getDatetimeActivated() == null) {
                        preparedStatement2.setString(17, null);
                    } else {
                        preparedStatement2.setString(17, wifiPoint.getDatetimeActivated().toString());
                    }
                    preparedStatement2.setInt(18, idToUpdate);
                    
                    preparedStatement2.execute();

                    System.out.println("Wifi Point successfully updated");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (oldPoint instanceof RetailerLocation) {
            statement1 = "SELECT id FROM retailer WHERE listid=? AND name=? AND addressLine1=? AND addressLine2=? AND city=? AND state=? AND " +
                    "zipcode=? AND blockLot=? AND primaryFunction=? AND secondaryFunction=? AND latitude=? AND longitude=?";
            try {
                int listid = getListID(username, listName, RetailerLocationList.class);

                RetailerLocation retailerLocation = (RetailerLocation) oldPoint; 
                preparedStatement1 = connection.prepareStatement(statement1);

                preparedStatement1.setInt(1, listid);
                preparedStatement1.setString(2, retailerLocation.getName());
                preparedStatement1.setString(3, retailerLocation.getAddressLine1());
                preparedStatement1.setString(4, retailerLocation.getAddressLine2());
                preparedStatement1.setString(5, retailerLocation.getCity());
                preparedStatement1.setString(6, retailerLocation.getState());
                preparedStatement1.setString(7, Integer.toString(retailerLocation.getZipcode()));
                preparedStatement1.setString(8, retailerLocation.getBlockLot());
                preparedStatement1.setString(9, retailerLocation.getPrimaryFunction());
                preparedStatement1.setString(10, retailerLocation.getSecondaryFunction());
                preparedStatement1.setFloat(11, retailerLocation.getLatitude());
                preparedStatement1.setFloat(12, retailerLocation.getLongitude());

                ResultSet rs = preparedStatement1.executeQuery();

                if (rs.next()) {
                    idToUpdate = rs.getInt(1);

                    statement2 = "UPDATE retailer SET listid=?, name=?, addressLine1=?, addressLine2=?, city=?, state=?, " +
                            "zipcode=?, blockLot=?, primaryFunction=?, secondaryFunction=?, latitude=?, longitude=? WHERE id=?";

                    preparedStatement2 = connection.prepareStatement(statement2);

                    RetailerLocation newRetailer = (RetailerLocation) newPoint;

                    preparedStatement2.setInt(1, listid);
                    preparedStatement2.setString(2, newRetailer.getName());
                    preparedStatement2.setString(3, newRetailer.getAddressLine1());
                    preparedStatement2.setString(4, newRetailer.getAddressLine2());
                    preparedStatement2.setString(5, newRetailer.getCity());
                    preparedStatement2.setString(6, newRetailer.getState());
                    preparedStatement2.setString(7, Integer.toString(newRetailer.getZipcode()));
                    preparedStatement2.setString(8, newRetailer.getBlockLot());
                    preparedStatement2.setString(9, newRetailer.getPrimaryFunction());
                    preparedStatement2.setString(10, newRetailer.getSecondaryFunction());
                    preparedStatement2.setFloat(11, newRetailer.getLatitude());
                    preparedStatement2.setFloat(12, newRetailer.getLongitude());
                    preparedStatement2.setInt(13, idToUpdate);

                    preparedStatement2.execute();

                    System.out.println("Retailer successfully updated");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (oldPoint instanceof BikeTrip) {
            statement1 = "SELECT id FROM trip WHERE listid=? AND duration=? AND startTime=? AND stopTime=? AND startLatitude=? AND " +
                    "startLongitude=? AND endLatitude=? AND endLongitude=? AND " +
                    "bikeID=? AND gender=? AND birthYear=? AND tripDistance=?";
            try {
                int listid = getListID(username, listName, BikeTripList.class);

                preparedStatement1 = connection.prepareStatement(statement1);

                BikeTrip oldTrip = (BikeTrip) oldPoint;

                preparedStatement1.setInt(1, listid);
                preparedStatement1.setLong(2, oldTrip.getTripDuration());
                preparedStatement1.setString(3, oldTrip.getStartTime().toString());
                preparedStatement1.setString(4, oldTrip.getStopTime().toString());
                preparedStatement1.setFloat(5, oldTrip.getStartLatitude());
                preparedStatement1.setFloat(6, oldTrip.getStartLongitude());
                preparedStatement1.setFloat(7, oldTrip.getEndLatitude());
                preparedStatement1.setFloat(8, oldTrip.getEndLongitude());
                preparedStatement1.setInt(9, oldTrip.getBikeId());
                preparedStatement1.setString(10, Character.toString(oldTrip.getGender()));
                preparedStatement1.setInt(11, oldTrip.getBirthYear());
                preparedStatement1.setDouble(12, oldTrip.getTripDistance());

                
                ResultSet rs = preparedStatement1.executeQuery();

                if (rs.next()) {
                    idToUpdate = rs.getInt(1);

                    statement2 = "UPDATE trip SET listid=?, duration=?, startTime=?, stopTime=?, startLatitude=?, " +
                            "startLongitude=?, endLatitude=?, endLongitude=?, " +
                            "bikeID=?, gender=?, birthYear=?, tripDistance=? WHERE id=?";

                    preparedStatement2 = connection.prepareStatement(statement2);

                    BikeTrip trip = (BikeTrip) newPoint;

                    preparedStatement2.setInt(1, listid);
                    preparedStatement2.setLong(2, trip.getTripDuration());
                    preparedStatement2.setString(3, trip.getStartTime().toString());
                    preparedStatement2.setString(4, trip.getStopTime().toString());
                    preparedStatement2.setFloat(5, trip.getStartLatitude());
                    preparedStatement2.setFloat(6, trip.getStartLongitude());
                    preparedStatement2.setFloat(7, trip.getEndLatitude());
                    preparedStatement2.setFloat(8, trip.getEndLongitude());
                    preparedStatement2.setInt(9, trip.getBikeId());
                    preparedStatement2.setString(10, Character.toString(trip.getGender()));
                    preparedStatement2.setInt(11, trip.getBirthYear());
                    preparedStatement2.setDouble(12, trip.getTripDistance());
                    preparedStatement2.setInt(13, idToUpdate);

                    preparedStatement2.execute();

                    System.out.println("Retailer successfully updated");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes a point from the database
     * @param username Owner of the list to delete the point from
     * @param listName Name of the list to delete the point from
     * @param point Point to delete
     */
    public static void deletePoint(String username, String listName, DataPoint point) {
        String statement;
        PreparedStatement preparedStatement;
        int listid;
        try {
            if (point instanceof WifiPoint) {
                statement = "DELETE FROM wifi WHERE listid=? AND latitude=? AND longitude=? AND ssid=?";
                listid = getListID(username, listName, WifiPointList.class);
                WifiPoint wifiPoint = (WifiPoint) point;

                preparedStatement = connection.prepareStatement(statement);

                preparedStatement.setInt(1, listid);
                preparedStatement.setFloat(2, wifiPoint.getLatitude());
                preparedStatement.setFloat(3, wifiPoint.getLongitude());
                preparedStatement.setString(4, wifiPoint.getSsid());

                preparedStatement.execute();

            } else if (point instanceof RetailerLocation) {
                statement = "DELETE FROM retailer WHERE listid=? AND name=? AND primaryFunction=?";
                listid = getListID(username, listName, RetailerLocationList.class);

                RetailerLocation retailerLocation= (RetailerLocation) point;

                preparedStatement = connection.prepareStatement(statement);

                preparedStatement.setInt(1, listid);
                preparedStatement.setString(2, retailerLocation.getName());
                preparedStatement.setString(3, retailerLocation.getPrimaryFunction());

                preparedStatement.execute();

            } else if (point instanceof BikeTrip) {
                statement = "DELETE FROM trip WHERE listid=? AND startLongitude=? AND startLatitude=? AND endLongitude=? AND endLatitude=? AND bikeid=?";
                listid = getListID(username, listName, BikeTripList.class);
                BikeTrip bikeTrip = (BikeTrip) point;

                preparedStatement = connection.prepareStatement(statement);

                preparedStatement.setInt(1, listid);
                preparedStatement.setFloat(2, bikeTrip.getStartLongitude());
                preparedStatement.setFloat(3, bikeTrip.getStartLatitude());
                preparedStatement.setFloat(4, bikeTrip.getEndLongitude());
                preparedStatement.setFloat(5, bikeTrip.getEndLatitude());
                preparedStatement.setInt(6, bikeTrip.getBikeId());

                preparedStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}