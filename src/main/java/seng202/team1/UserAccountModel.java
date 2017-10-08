package seng202.team1;

import seng202.team1.Controller.AlertGenerator;
import seng202.team1.Model.*;
import seng202.team1.Model.CsvHandling.CSVLoader;
import seng202.team1.Model.CsvHandling.CsvParserException;

import java.awt.*;
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
            addPointList(new BikeTripList("Default", CSVLoader.populateBikeTrips()));
            addPointList(new RetailerLocationList("Default", CSVLoader.populateRetailers()));
            addPointList(new WifiPointList("Default", CSVLoader.populateWifiHotspots()));
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
    public boolean changePassword(String currentPassword, String newPassword, String confirmPassword, UserAccountModel model) {
        if (PasswordManager.isExpectedPassword(currentPassword, salt, password)) {
            if (!PasswordManager.isExpectedPassword(newPassword, salt, password)) {
                if (newPassword.equals(confirmPassword)) {
                    byte[] holdingSalt = PasswordManager.getNextSalt();
                    this.salt = holdingSalt;
                    byte[] holdingPassword = PasswordManager.hash(newPassword, salt);
                    this.password = holdingPassword;
                    SerializerImplementation.serializeUser(model);
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


    public BikeTripList getBikeTripsFromList(String listName) {
        ArrayList<BikeTrip> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getBikeTrips(userName, listName);
            System.out.println(String.format("%d custom trips retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new BikeTripList(listName, result);
    }


    public WifiPointList getWifiPointsFromList(String listName) {
        ArrayList<WifiPoint> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getWifiPoints(userName, listName);
            System.out.println(String.format("%d custom trips retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new WifiPointList(listName, result);
    }


    public RetailerLocationList getRetailerPointsFromList(String listName) {
        ArrayList<RetailerLocation> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getRetailers(userName, listName);
            System.out.println(String.format("%d custom retailers retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new RetailerLocationList(listName, result);
    }


    public void addPoint(DataPoint point, String listName) {
        try {
            DatabaseManager.open();
            DatabaseManager.addRecord(point, userName, listName);
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addPointList(PointList pointList) {
        Class pointListClass = null;
        if (pointList instanceof WifiPointList) {
            pointListClass = WifiPointList.class;
        } else if (pointList instanceof RetailerLocationList) {
            pointListClass = RetailerLocationList.class;
        } else if (pointList instanceof BikeTripList) {
            pointListClass = BikeTripList.class;
        }

        try {
            DatabaseManager.open();
            DatabaseManager.createNewList(userName, pointList.getListName(), pointListClass);
            DatabaseManager.populateList(userName, pointList);
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SerializerImplementation.serializeUser(this);
    }


    public ArrayList<BikeTripList> getBikeTripLists() {
        ArrayList<String> results = getListNamesOfType(BikeTripList.class);
        ArrayList<BikeTripList> output = new ArrayList<>();
        for (String result : results) {
            output.add(getBikeTripsFromList(result));
        }
        return output;
    }


    public ArrayList<RetailerLocationList> getRetailerLocationLists() {
        ArrayList<String> results = getListNamesOfType(RetailerLocationList.class);
        ArrayList<RetailerLocationList> output = new ArrayList<>();
        for (String result : results) {
            output.add(getRetailerPointsFromList(result));
        }
        return output;
    }


    public ArrayList<WifiPointList> getWifiPointLists() {
        ArrayList<String> results = getListNamesOfType(WifiPointList.class);
        ArrayList<WifiPointList> output = new ArrayList<>();
        for (String result : results) {
            output.add(getWifiPointsFromList(result));
        }
        return output;
    }


    public ArrayList<String> getListNamesOfType(Class type) {
        ArrayList<String> results = new ArrayList<>();
        try {
            DatabaseManager.open();
            results = DatabaseManager.getLists(userName, type);
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    public static void createUser(UserAccountModel user) {
        SerializerImplementation.serializeUser(user);
    }


    public static UserAccountModel getUser(String username) throws IOException {
        return SerializerImplementation.deserializeUser(username);
    }
}