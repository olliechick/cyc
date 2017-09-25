package seng202.team1.Model;

import seng202.team1.Model.BikeTrip;
import seng202.team1.Model.RetailerLocation;
import seng202.team1.Model.WifiPoint;

import java.util.ArrayList;

/**
 * A static class that detects duplicates and provides input validation.
 *
 * @author Josh Burt
 */
public final class InputValidator {


    /**
     * Takes a test bike trip and a list of biketrips and returns true if the provided trip is a duplicate
     * @param trip bike trip to be checked
     * @param bikeTrips list of trips to check against
     * @return true if duplicate, false if not
     */
    public static boolean isDuplicateBikeTrip(BikeTrip trip, ArrayList<BikeTrip> bikeTrips){
        for (BikeTrip testTrip : bikeTrips){
            if (testTrip.equals(trip)){
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a test WifiPoint and a list of WifiPoints and returns true if it is a duplicate
     * @param wifiPoint point to test
     * @param hotspots list to test against
     * @return true if duplicate, false if not
     */
    public static boolean isDuplicateWifiPoint(WifiPoint wifiPoint, ArrayList<WifiPoint> hotspots){
        for (WifiPoint hotspot: hotspots){
            if(hotspot.equals(wifiPoint)){
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a RetailerLocation and a list of RetailerLocations and returns true if it is a duplicate.
     * @param shop Retailer to test
     * @param retailers list to test against
     * @return true if duplicate, false if not
     */
    public static boolean isDuplicateRetailer(RetailerLocation shop, ArrayList<RetailerLocation> retailers){
        for (RetailerLocation retailer : retailers){
            if (retailer.equals(shop)){
                return true;
            }
        }
        return false;
    }
}
