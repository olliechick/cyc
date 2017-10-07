package seng202.team1.Model;

import java.util.ArrayList;

/**
 * Created on 5/10/17.
 *
 * Data structure for containing a list of points and a name.
 * Superclass of WifiPointList, RetailerLocationList, and BikeTripList.
 *
 * @author Ridge Nairn
 */
public class PointList implements java.io.Serializable {

    private String listName;

    public PointList(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }
}
