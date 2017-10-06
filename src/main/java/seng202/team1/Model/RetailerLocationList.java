package seng202.team1.Model;

import java.util.ArrayList;

/**
 * Created by jbe113 on 5/10/17.
 */
public class RetailerLocationList implements java.io.Serializable {

    private ArrayList<RetailerLocation> retailerLocations = new ArrayList<>();
    private String listName;

    public RetailerLocationList(String listName, ArrayList<RetailerLocation> locations) {
        this.listName = listName;
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

    public String getListName() {
        return listName;
    }
}
