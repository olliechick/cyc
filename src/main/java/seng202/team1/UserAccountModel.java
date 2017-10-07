package seng202.team1;

import seng202.team1.Controller.AlertGenerator;
import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.BikeTripList;
import seng202.team1.Model.DataPoint;
import seng202.team1.Model.DatabaseManager;
import seng202.team1.Model.PasswordManager;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.RetailerLocationList;
import seng202.team1.Model.SerializerImplementation;
import seng202.team1.Model.WifiPoint;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.CsvHandling.CsvParserException;
import seng202.team1.Model.WifiPointList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

/**
 * Model class of a user.
 * Has 4 constructors for various options, including account type specification and optional gender.
 * Flags any user who is under 13 (for data usage laws).
 * Implements Serializable to allow Users to be persistently saved.
 *
 * @author Josh Burt
 * @author Ollie Chick
 * @author Ridge Nairn
 * @author Josh Bernasconi
 */
public class UserAccountModel implements java.io.Serializable {

    private char gender;
    private LocalDate birthday;
    private boolean under13;
    private String userName;
    private byte[] password;
    private byte[] salt;
    private ArrayList<BikeTripList> bikeTripLists = new ArrayList<>();
    private ArrayList<RetailerLocationList> retailerLocationLists = new ArrayList<>();
    private ArrayList<WifiPointList> wifiPointLists = new ArrayList<>();

    /**
     * Constructor with account type set to "User".
     */
    public UserAccountModel(char gender, LocalDate birthday, String userName, String password) {
        this.gender = gender;
        this.birthday = birthday;
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthday, currentDate).getYears();
        this.under13 = age < 13;
        this.userName = userName;
        this.salt = PasswordManager.getNextSalt();
        this.password = PasswordManager.hash(password, salt);
        createDefaultLists();
    }

    /**
     * Constructor with account type set to "User" and gender set to unknown.
     */
    public UserAccountModel(LocalDate birthday, String userName, String password) {
        this.birthday = birthday;
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthday, currentDate).getYears();
        this.under13 = age < 13;
        this.userName = userName;
        this.salt = PasswordManager.getNextSalt();
        this.password = PasswordManager.hash(password, salt);
        this.gender = 'u';
        createDefaultLists();
    }

    //TODO test this

    /**
     * Create the default lists of points for a user, so that they start with something to
     * display in a table.
     */
    private void createDefaultLists() {
        try {
            BikeTripList defaultTrips = new BikeTripList("Default", CSVLoader.populateBikeTrips());
            bikeTripLists.add(defaultTrips);
            RetailerLocationList defaultRetailers = new RetailerLocationList("Default", CSVLoader.populateRetailers());
            retailerLocationLists.add(defaultRetailers);
            WifiPointList defaultWifis = new WifiPointList("Default", CSVLoader.populateWifiHotspots());
            wifiPointLists.add(defaultWifis);
        } catch (CsvParserException | IOException e) {
            AlertGenerator.createAlert("Some of the default lists could not be created!");
        }
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        if (gender == 'f' || gender == 'm') {
            this.gender = gender;
        } else {
            this.gender = 'u';
        }
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isUnder13() {
        return under13;
    }

    public void setUnder13(boolean under13) {
        this.under13 = under13;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPassword() {
        return password;
    }


    public byte[] getSalt() {
        return this.salt;
    }

    /**
     * Allows the Users password to be changed
     * @param currentPassword The users current password
     * @param newPassword The desired new Password
     * @param confirmPassword The confirmation of the old password
     * @return True if Changed false if not
     */
    public boolean changePassword(String currentPassword, String newPassword, String confirmPassword){
        if (PasswordManager.isExpectedPassword(currentPassword, salt, password)) {
            if (!PasswordManager.isExpectedPassword(newPassword, salt, password)){
            if (newPassword.equals(confirmPassword)) {
                salt = PasswordManager.getNextSalt();
                password = PasswordManager.hash(newPassword, salt);
                return true;
            } else {
                AlertGenerator.createAlert("New passwords do not match");
            }
        } else {
                AlertGenerator.createAlert("Your new password cannot be the same as your old password");
            }
        } else {
            AlertGenerator.createAlert("The current password is incorrect");
        }
        return false;
    }

//from dummy Model


    public ArrayList<BikeTrip> getCustomBikeTrips() {
        ArrayList<BikeTrip> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getBikeTrips(userName);
            System.out.println(String.format("%d custom trips retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<WifiPoint> getCustomWifiPoints() {
        ArrayList<WifiPoint> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getWifiPoints(userName);
            System.out.println(String.format("%d custom wifi points retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }


    @Deprecated // To be replaced with method below
    public ArrayList<RetailerLocation> getCustomRetailerLocations() {
        ArrayList<RetailerLocation> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getRetailers(userName); //TODO: Get current listName?
            System.out.println(String.format("%d custom retailers retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<RetailerLocation> getCustomRetailerLocations(String listName) {
        ArrayList<RetailerLocation> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getRetailers(userName, listName); //TODO: Get current listName?
            System.out.println(String.format("%d custom retailers retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addCustomBikeTrip(BikeTrip bikeTrip) {
        addPoint(bikeTrip);
    }

    public void addCustomRetailerLocation(RetailerLocation retailerLocation) {
        addPoint(retailerLocation);
    }

    public void addCustomWifiLocation(WifiPoint wifiPoint) {
        addPoint(wifiPoint);
    }

    private void addPoint(DataPoint point) {
        try {
            //TODO: Have this thrown further up
            DatabaseManager.open();
            DatabaseManager.addRecord(point, userName, "My List of Trips");
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BikeTripList> getBikeTripLists() {
        return bikeTripLists;
    }

    public ArrayList<RetailerLocationList> getRetailerLocationLists() {
        return retailerLocationLists;
    }

    public ArrayList<WifiPointList> getWifiPointLists() {
        return wifiPointLists;
    }

    public static void createUser(UserAccountModel user) {
        SerializerImplementation.serializeUser(user);
    }

    public static UserAccountModel getUser(String username) throws IOException {
        return SerializerImplementation.deserializeUser(username);
    }
}

