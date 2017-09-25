package seng202.team1;

import java.io.IOException;
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
    private String accountType;
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
        this.accountType = "User";
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
        this.accountType = "User";
        this.userName = userName;
        this.salt = PasswordManager.getNextSalt();
        this.password = PasswordManager.hash(password, salt);
        this.gender = 'u';
    }

    /**
     * Constructor with all parameters set by caller.
     */
    public UserAccountModel(char gender, String accountType, LocalDate birthday, String userName, String password) {
        this.gender = gender;
        this.accountType = accountType;
        this.birthday = birthday;
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthday, currentDate).getYears();
        this.under13 = age < 13;

        this.userName = userName;
        this.salt = PasswordManager.getNextSalt();
        this.password = PasswordManager.hash(password, salt);
    }

    /**
     * Constructor with gender set to unknown.
     */
    public UserAccountModel(String accountType, LocalDate birthday, String userName, String password) {
        this.accountType = accountType;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        if (accountType.equalsIgnoreCase("user") || accountType.equalsIgnoreCase("analyst") || accountType.equalsIgnoreCase("admin")) {
            this.accountType = accountType;
        } else {
            this.accountType = "User";
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

    public void setPassword(String password) {
        this.salt = PasswordManager.getNextSalt();
        this.password = PasswordManager.hash(password, salt);
    }

    public byte[] getSalt() {
        return this.salt;
    }

//from dummy Model

    private ArrayList<BikeTrip> customBikeTrips = new ArrayList<>();
    private ArrayList<RetailerLocation> customRetailerLocations = new ArrayList<>();
    private ArrayList<WifiPoint> customWifiPoints = new ArrayList<>();

    public ArrayList<BikeTrip> getCustomBikeTrips() {
        return customBikeTrips;
    }

    public ArrayList<RetailerLocation> getCustomRetailerLocations() {
        return customRetailerLocations;
    }

    public ArrayList<WifiPoint> getCustomWifiPoints() {
        return customWifiPoints;
    }

    public void addCustomBikeTrip(BikeTrip bikeTrip) {
        customBikeTrips.add(bikeTrip);
    }

    public void addCustomRetailerLocation(RetailerLocation retailerLocation) {
        customRetailerLocations.add(retailerLocation);
    }

    public void addCustomWifiLocation(WifiPoint wifiPoint) {
        customWifiPoints.add(wifiPoint);
    }

    public static void createUser(UserAccountModel user) {
        SerializerImplementation.serializeUser(user);
    }

    public static UserAccountModel getUser(String username) throws IOException {
        return SerializerImplementation.deserializeUser(username);
    }
}

