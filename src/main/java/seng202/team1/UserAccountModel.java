package seng202.team1;

import seng202.team1.Controller.AlertGenerator;
import seng202.team1.Model.*;

import java.io.File;
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
            if (newPassword.equals(confirmPassword)) {
                salt = PasswordManager.getNextSalt();
                password = PasswordManager.hash(newPassword, salt);
                return true;
            } else {
                AlertGenerator.createAlert("New passwords do not match");
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
            result = DatabaseManager.getUserTrips(userName);
            System.out.println(String.format("%d custom trips retrieved.", result.size()));
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<RetailerLocation> getCustomRetailerLocations() {
        ArrayList<RetailerLocation> result = new ArrayList<>();
        try {
            DatabaseManager.open();
            result = DatabaseManager.getRetailers(userName);
            System.out.println(String.format("%d custom retailers retrieved.", result.size()));
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

    public void addCustomBikeTrip(BikeTrip bikeTrip) {
        try {
            //TODO: Have this thrown further up
            DatabaseManager.open();
            DatabaseManager.addBikeTrip(bikeTrip, userName);
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomRetailerLocation(RetailerLocation retailerLocation) {
        try {
            //TODO: Have this thrown further up
            DatabaseManager.open();
            DatabaseManager.addRecord(retailerLocation, userName);
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomWifiLocation(WifiPoint wifiPoint) {
        try {
            //TODO: Have this thrown further up
            DatabaseManager.open();
            DatabaseManager.addRecord(wifiPoint, userName);
            DatabaseManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createUser(UserAccountModel user) {
        SerializerImplementation.serializeUser(user);
    }

    public static UserAccountModel getUser(String username) throws IOException {
        return SerializerImplementation.deserializeUser(username);
    }
}

