package seng202.team1;

import java.awt.Point;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private String googleData;

    //default constructor
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeID, char gender, int birthYear,
                    boolean isUserDefinedPoint) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthYear = birthYear;
        this.isUserDefinedPoint = isUserDefinedPoint;
    }

    //constructor that calculates tripDuration
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime, Point.Float startPoint,
                    Point.Float endPoint, int bikeID, char gender, int birthYear, boolean isUserDefinedPoint) {
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthYear = birthYear;
        this.isUserDefinedPoint = isUserDefinedPoint;
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

    public String getGoogleData() {
        return googleData;
    }

    public void setGoogleData(String googleData) {
        this.googleData = googleData;
    }

    /**
     * Returns the duration of the trip, contextualised.
     * Either in seconds, minutes, hours, or days (rounded down).
     */
    public String getDuration() {
        String duration;
        // first find the unit of time and how many units
        String unit;
        long unitCount;
        if (tripDuration < 60) {
            unit = "second";
            unitCount = tripDuration;
        } else if (tripDuration < 60*60) {
            unit = "minute";
            unitCount = tripDuration / 60;
        } else if (tripDuration < 60*60*24) {
            unit = "hour";
            unitCount = tripDuration / (60*60);
        } else {
            unit = "day";
            unitCount = tripDuration / (60 * 60 * 24);
        }

        duration = unitCount + " " + unit;

        // then check if it's plural
        if (unitCount != 1) {
            duration += "s";
        }

        return duration;
    }


    /**
     * Returns the gender description (male, female, or unknown).
     */
    public String getGenderDescription() {

        String genderDescription;

        if (gender == 'm') {
            genderDescription = "male";
        } else if (gender == 'f') {
            genderDescription = "female";
        } else {
            genderDescription = "unknown";
        }

        return genderDescription;
    }


    public String getName() {
        String start = startTime.format(DateTimeFormatter.ofPattern("h:mm a d MMMM yyyy"))
                .replace("AM", "am").replace("PM","pm");
        return String.format("Trip at %s", start);
    }



    /**
     * Returns a description of the bike trip.
     */
    public String getDescription() {
        String start = startTime.format(DateTimeFormatter.ofPattern("h:mm a d MMMM yyyy"))
                .replace("AM", "am").replace("PM","pm");

        // end: check if including the date (or year) is necessary
        String end;
        if (startTime.getYear() != stopTime.getYear()) {
            // different years
            end = stopTime.format(DateTimeFormatter.ofPattern("h:mm a d MMMM yyyy"));
        } else if (startTime.getDayOfYear() != stopTime.getDayOfYear()) {
            //different days
            end = stopTime.format(DateTimeFormatter.ofPattern("h:mm a d MMMM"));
        } else {
            //same day
            end = stopTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        }
        end = end.replace("AM", "am").replace("PM","pm");

        // Put together description

        return String.format("Started at %s and ended %s later at %s\nBike ID: %d\nCyclist: %s, born in %d",
                start, getDuration(), end, bikeID, getGenderDescription(), birthYear);
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
