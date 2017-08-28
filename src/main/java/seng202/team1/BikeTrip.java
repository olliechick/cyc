package seng202.team1;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Bike Trip data class. This will likely change during development as we need to do more.
 * So far it just builds a bike trip from given data.
 * @author Josh Burt
 * @author Ollie Chick
 */
public class BikeTrip extends DataPoint {

    private long tripDuration; //in seconds
    private LocalDateTime startTime; //example usage: LocalDateTime aDateTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
    private LocalDateTime stopTime;
    private Point.Float startPoint;
    private Point.Float endPoint;
    private int bikeID;
    private char gender; //u for unknown, m for male, f for female
    private int birthyear;

    //default constructor
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime, Point.Float startPoint, Point.Float endPoint, int bikeID, char gender, int birthyear) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthyear = birthyear;
    }

    //constructor that calculates tripDuration
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime, Point.Float startPoint, Point.Float endPoint, int bikeID, char gender, int birthyear) {
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthyear = birthyear;
    }

    public long getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(long tripDuration) {
        this.tripDuration = tripDuration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public Point.Float getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point.Float startPoint) {
        this.startPoint = startPoint;
    }

    public Point.Float getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point.Float endPoint) {
        this.endPoint = endPoint;
    }

    public int getBikeID() {
        return bikeID;
    }

    public void setBikeID(int bikeID) {
        this.bikeID = bikeID;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    @Override
    public String toString() {
        return "BikeTrip{" +
                "tripDuration=" + tripDuration +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", bikeID=" + bikeID +
                ", gender=" + gender +
                ", birthyear=" + birthyear +
                '}';
    }

}
