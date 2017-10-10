package seng202.team1.Model;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;

public class RetailerPointDistance {
    private RetailerLocation thisPoint;
    private Point2D.Double otherPoint;
    private Double distance;
    private Integer indexMap;
    public String name;
    public String secondaryFunction;
    public String primaryFunction;

    public Integer getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(Integer indexMap) {
        this.indexMap = indexMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryFunction() {
        return primaryFunction;
    }

    public String getSecondaryFunction() {
        return secondaryFunction;
    }

    public void setSecondaryFunction(String secondaryFunction) {
        this.secondaryFunction = secondaryFunction;
    }

    public RetailerPointDistance(RetailerLocation thisPoint, Point2D.Double otherPoint, Double distance, int indexMap) {
        this.thisPoint = thisPoint;
        this.otherPoint = otherPoint;
        this.distance = distance;
        this.indexMap = indexMap;
        this.name = thisPoint.getName();
        this.secondaryFunction = thisPoint.getSecondaryFunction();
        this.primaryFunction = thisPoint.getPrimaryFunction();

    }
    public RetailerPointDistance(RetailerLocation thisPoint, int indexMap) {
        this.thisPoint = thisPoint;
        this.otherPoint = new Point2D.Double(-1, -1);
        this.distance = Double.POSITIVE_INFINITY;
        this.indexMap = indexMap;
        this.name = thisPoint.getName();
        this.secondaryFunction = thisPoint.getSecondaryFunction();
        this.primaryFunction = thisPoint.getPrimaryFunction();
    }

    public RetailerLocation getThisPoint() {

        return thisPoint;
    }

    public void setThisPoint(RetailerLocation thisPoint) {
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

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "RetailerPointDistance{" +
                " distance=" + distance +
                ",thisPoint=" + thisPoint +
                ", otherPoint=" + otherPoint +
                ", indexMap=" + indexMap +
                '}';
    }
}
