package seng202.team1.Model;

import java.awt.*;
import java.awt.geom.Point2D;

public class RetailerPointDistance {
    private RetailerLocation thisPoint;
    private Point2D.Double otherPoint;
    private Double distance;
    private Integer indexMap;

    public Integer getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(Integer indexMap) {
        this.indexMap = indexMap;
    }

    public RetailerPointDistance(RetailerLocation thisPoint, Point2D.Double otherPoint, Double distance, int indexMap) {
        this.thisPoint = thisPoint;
        this.otherPoint = otherPoint;
        this.distance = distance;
        this.indexMap = indexMap;
    }
    public RetailerPointDistance(RetailerLocation thisPoint, int indexMap) {
        this.thisPoint = thisPoint;
        this.otherPoint = new Point2D.Double(-1, -1);
        this.distance = Double.POSITIVE_INFINITY;
        this.indexMap = indexMap;
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