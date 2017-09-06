package seng202.team1;

import java.awt.Point;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Bike Trip data class. This will likely change during development as we need to do more.
 * So far it just builds a bike trip from given data.
 * If birthYear = -1, this is a flag which means the data is not available.
 * Gender will be m (male), f (female), or u (unknown).
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
    private int birthYear;

    //default constructor
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeID, char gender, int birthYear) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    //constructor that calculates tripDuration
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime, Point.Float startPoint,
                    Point.Float endPoint, int bikeID, char gender, int birthYear) {
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthYear = birthYear;
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

    // Start point

    public Point.Float getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point.Float startPoint) {
        this.startPoint = startPoint;
    }

    public float getStartLongitude() {
        return startPoint.x;
    }

    public void setStartLongitude(float startLongitude) {
        this.startPoint.x = startLongitude;
    }

    public float getStartLatitude() {
        return startPoint.y;
    }

    public void setStartLatitude(float startLatitude) {
        this.startPoint.y = startLatitude;
    }

    // End point

    public Point.Float getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point.Float endPoint) {
        this.endPoint = endPoint;
    }

    public float getEndLongitude() {
        return endPoint.x;
    }

    public void setEndLongitude(float endLongitude) {
        this.endPoint.x = endLongitude;
    }

    public float getEndLatitude() {
        return endPoint.y;
    }

    public void setEndLatitude(float endLatitude) {
        this.endPoint.y = endLatitude;
    }

    //Other

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

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
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
                ", birthYear=" + birthYear +
                '}';
    }

}
