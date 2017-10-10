package seng202.team1.Model;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;

public class WIFIPointDistance {
    private WifiPoint thisPoint;
    private Point2D.Double otherPoint;
    private Double distance;
    private Integer indexMap;

    public Integer getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(Integer indexMap) {
        this.indexMap = indexMap;
    }

    public WIFIPointDistance(WifiPoint thisPoint, Point2D.Double otherPoint, Double distance, int indexMap) {
        this.thisPoint = thisPoint;
        this.otherPoint = otherPoint;
        this.distance = distance;
        this.indexMap = indexMap;

    }
    public WIFIPointDistance(WifiPoint thisPoint, int indexMap) {
        this.thisPoint = thisPoint;
        this.otherPoint = new Point2D.Double(-1, -1);
        this.distance = Double.POSITIVE_INFINITY;
        this.indexMap = indexMap;
    }

    public WifiPoint getThisPoint() {

        return thisPoint;
    }

    public String getName() {return thisPoint.getName();}

    public void setThisPoint(WifiPoint thisPoint) {
        this.thisPoint = thisPoint;
    }

    public Point2D.Double getOtherPoint() {
        return otherPoint;
    }

    public void setOtherPoint(Point2D.Double otherPoint) {
        this.otherPoint = otherPoint;
    }


    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getCost() {return thisPoint.getCost();}

    public String getProvider() {return thisPoint.getProvider();}

    public String getSSID() {return thisPoint.getSsid();}



    /**
     * @return trip distance to decimal places
     */
    public String getTripDistanceTwoD() {
        String distanceString;

        if (distance < 10) {
            //single digit metres (and below)
            distanceString = "" + Double.parseDouble(new DecimalFormat("#.##").format(distance));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " m";
        } else if (distance < 100) {
            //double digit metres
            distanceString = "" + Double.parseDouble(new DecimalFormat("##.#").format(distance));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " m";
        } else if (distance < 1e3) {
            //three digit metres
            distanceString = Math.round(distance) + " m";
        } else if (distance < 1e4) {
            //single digit km
            distanceString = "" + Double.parseDouble(new DecimalFormat("#.##").format(distance / 1e3));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " km";
        } else if (distance < 1e5) {
            //double digit km
            distanceString = "" + Double.parseDouble(new DecimalFormat("##.#").format(distance / 1e3));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " km";
        } else {
            //triple digit km (and above)
            distanceString = Math.round(distance / 1e3) + " km";
        }

        return distanceString;
    }

    @Override
    public String toString() {
        return "WIFIPointDistance{" +
                " distance=" + distance +
                ",thisPoint=" + thisPoint +
                ", otherPoint=" + otherPoint +
                ", indexMap=" + indexMap +
                '}';
    }
}
