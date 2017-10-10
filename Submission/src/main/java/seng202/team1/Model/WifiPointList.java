package seng202.team1.Model;

import java.util.ArrayList;

/**
 * Created on 5/10/17.
 *
 * Data structure for containing a list of points and a name
 *
 * @author Josh Bernasconi
 */
public class WifiPointList extends PointList implements java.io.Serializable {

    private ArrayList<WifiPoint> wifiPoints = new ArrayList<>();

    public WifiPointList(String listName, ArrayList<WifiPoint> wifiPoints) {
        super(listName);
        this.wifiPoints = wifiPoints;
    }

    public ArrayList<WifiPoint> getWifiPoints() {
        return wifiPoints;
    }

    public void addWifi(WifiPoint wifiPoint) {
        wifiPoints.add(wifiPoint);
    }

    public void removeWifi(WifiPoint wifiPoint) {
        wifiPoints.remove(wifiPoint);
    }

}
