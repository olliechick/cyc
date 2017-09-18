package seng202.team1.Controller;

import seng202.team1.BikeTrip;
import seng202.team1.RetailerLocation;
import seng202.team1.WifiPoint;

import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateWifiHotspots;

/**
 * Dummy model class
 * For testing only
 *
 * Created by jbe113 on 22/08/17.
 */
public class DummyModel {

    private String name;

    public void setName(String inName) {
        name = inName;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public ArrayList<BikeTrip> bikeTrips = new ArrayList<BikeTrip>();

    public ArrayList<WifiPoint> wifiPoints = new ArrayList<WifiPoint>();

    public ArrayList<RetailerLocation> retailerLocations = new ArrayList<RetailerLocation>();

    public void loadAllWifiPoints() {
        wifiPoints =  populateWifiHotspots("src/main/resources/csv/NYC_Free_Public_WiFi_03292017.csv");
    }


}
