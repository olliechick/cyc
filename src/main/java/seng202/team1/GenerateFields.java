package seng202.team1;

import java.util.ArrayList;

/**
 * Generates data fields and other related tasks for the GUI.
 * @author Josh Burt
 */
public final class GenerateFields {

    /**
     * Takes a list of retailers and returns all the unique primary functions.
     * returns an empty list if no primary functions are found.
     * Be aware this function is O(nm) so if too many primary functions are found it will blow out time wise
     * @param retailers ArrayList Of RetailerLocations that need to have primary functions extracted
     * @return ArrayList<String>
     */
    public static ArrayList<String> generatePrimaryFunctionsList(ArrayList<RetailerLocation> retailers){
        ArrayList<String> primaryFunctions = new ArrayList<String>();
        for (RetailerLocation retailer : retailers){
            boolean found = false;
            for (String function :primaryFunctions) {
                if (function.equalsIgnoreCase(retailer.getPrimaryFunction())){
                    found = true;
                    break;
                }

            }
            if (!found){
                primaryFunctions.add(retailer.getPrimaryFunction());
            }
        }
        return primaryFunctions;
    }

    /**
     * Takes a list of retailers and returns their secondary Functions.
     * Speed can be improved by passing it a list consisting of only the same primary function
     * Be aware this function is O(nm) so if too many secondary functions are found it will blow out time wise
     * @param retailers ArrayList Of RetailerLocations that need to have secondary functions extracted
     * @return ArrayList<String>
     */
    public static ArrayList<String> generateSecondaryFunctionsList(ArrayList<RetailerLocation> retailers){
        ArrayList<String> secondaryFunctions = new ArrayList<String>();
        for (RetailerLocation retailer : retailers){
            boolean found = false;
            for (String function :secondaryFunctions) {
                if (function.equalsIgnoreCase(retailer.getSecondaryFunction())){
                    found = true;
                    break;
                }

            }
            if (!found){
                secondaryFunctions.add(retailer.getSecondaryFunction());
            }
        }
        return secondaryFunctions;
    }

    /**
     * Takes a list of retailers and the function name to sort them by and returns list that contains only retailers of the same function
     * the isPrimary flag allows the function to be used either for the primary or secondary function to be selected
     * with true meaning primary and false being secondary.
     * @param retailers List of reatailer that need the functions checked against
     * @param function Type of retailer that needs to be found
     * @param isPrimary Flag for weather to find the primary or secondary function
     * @return ArrayList<RetailerLocation>
     */
    public static ArrayList<RetailerLocation> generateListOfSameFunction(ArrayList<RetailerLocation> retailers,
                                                                         String function, boolean isPrimary){
        ArrayList<RetailerLocation> sameFunction = new ArrayList<RetailerLocation>();
        if(isPrimary){
            for (RetailerLocation retailer : retailers){
                if (retailer.getPrimaryFunction().equalsIgnoreCase(function)){
                    sameFunction.add(retailer);
                }
            }
        } else {
            for (RetailerLocation retailer : retailers){
                if (retailer.getSecondaryFunction().equalsIgnoreCase(function)){
                    sameFunction.add(retailer);
                }
            }
        }
        return sameFunction;
    }

    /**
     * Takes a list of wifi Hotspots and returns the types of cost they have.
     * @param hotspots list of WifiPoints to search through
     * @return ArrayList<String>
     */
    public static ArrayList<String> generateWifiTypes(ArrayList<WifiPoint> hotspots){
        ArrayList<String> wifiTypes = new ArrayList<String>();
        for(WifiPoint hotspot : hotspots){
            boolean found = false;
            for(String type : wifiTypes){
                if (type.equalsIgnoreCase(hotspot.getCost())){
                    found = true;
                    break;
                }
            }
            if (!found){
                wifiTypes.add(hotspot.getCost());
            }
        }
        return wifiTypes;
    }

    /**
     * Takes a list of wifi hotspots and and string containing the cost and returns all those that match
     * case insensative
     * @param cost type of cost to look for - "free", "Limited Free",etc...
     * @param hotspots list of hotspots to go through
     * @return ArrayList<WifiPoint>
     */
    public static ArrayList<WifiPoint> findPointsOfSameCost(String cost, ArrayList<WifiPoint> hotspots){
        ArrayList<WifiPoint> results = new ArrayList<WifiPoint>();
        for (WifiPoint hotspot : hotspots){
            if (cost.equalsIgnoreCase(hotspot.getCost())){
                results.add(hotspot);
            }
        }
        return results;
    }

    /**
     * Takes an arraylist of wifi hotspots and returns all the providers present.
     * @param hotspots list of hotspots to search through
     * @return ArrayList<String>
     */
    public static ArrayList<String> generateWifiProviders(ArrayList<WifiPoint> hotspots){
        ArrayList<String> wifiTypes = new ArrayList<String>();
        for(WifiPoint hotspot : hotspots){
            boolean found = false;
            for(String provider : wifiTypes){
                if (provider.equalsIgnoreCase(hotspot.getProvider())){
                    found = true;
                    break;
                }
            }
            if (!found){
                wifiTypes.add(hotspot.getProvider());
            }
        }
        return wifiTypes;
    }

    /**
     * Takes a wifi porivder and a list of hotspots and returns all the hotspots in the list that have that provider
     * @param provider provider to search for
     * @param hotspots List of hotspots to search through
     * @return ArrayList<WifiPoint>
     */
    public static ArrayList<WifiPoint> findWifiOfSameProvider(String provider,
                                                              ArrayList<WifiPoint> hotspots){
        ArrayList<WifiPoint> results = new ArrayList<WifiPoint>();
        for(WifiPoint hotspot : hotspots){
            if (provider.equalsIgnoreCase(hotspot.getProvider())){
                results.add(hotspot);
            }
        }

        return results;
    }
}
