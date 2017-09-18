package seng202.team1.Controller;

import seng202.team1.BikeTrip;
import seng202.team1.RetailerLocation;
import seng202.team1.WifiPoint;

import java.util.ArrayList;

/**
 * Dummy model class
 * For testing only
 *
 * Created on 22/08/17.
 * @author Josh Bernasconi
 */
public class DummyModel {

    private String name;

    private ArrayList<BikeTrip> customBikeTrips;
    private ArrayList<RetailerLocation> customRetailerLocations;
    private ArrayList<WifiPoint> customWifiPoints;

    public void setName(String inName) {
        name = inName;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

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

}
