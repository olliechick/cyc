package seng202.team1.Model;

import java.awt.*;
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

    public String getSSID() {return thisPoint.getSsid();}
    /**
     * @return trip distance to decimal places
     */
    public Double getTripDistanceTwoD() {
        return Double.parseDouble(new DecimalFormat("#.##").format(distance));
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
