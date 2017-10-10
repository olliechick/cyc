package seng202.team1.Model;

import java.util.ArrayList;

/**
 * Created on 5/10/17.
 *
 * Data structure for containing a list of points and a name
 *
 * @author Josh Bernasconi
 */
public class RetailerLocationList extends PointList implements java.io.Serializable {

    private ArrayList<RetailerLocation> retailerLocations = new ArrayList<>();

    public RetailerLocationList(String listName, ArrayList<RetailerLocation> locations) {
        super(listName);
        this.retailerLocations = locations;
    }

    public ArrayList<RetailerLocation> getRetailerLocations() {
        return retailerLocations;
    }

    public void addRetailer(RetailerLocation retailerLocation) {
        retailerLocations.add(retailerLocation);
    }

    public void removeRetailer(RetailerLocation retailerLocation) {
        retailerLocations.remove(retailerLocation);
    }

}
