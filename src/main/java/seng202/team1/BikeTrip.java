package seng202.team1;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Bike Trip data class.
 * If birthYear = -1, this is a flag which means the data is not available.
 * Gender will be m (male), f (female), or u (unknown).
 * @author Josh Burt
 * @author Ollie Chick
 */
public class BikeTrip extends DataPoint {

    private final static String DT_FORMAT = "h:mm a d MMMM yyyy";
    private final static String DAY_OF_MONTH_FORMAT = "h:mm a d MMMM";
    private final static String TIME_FORMAT = "h:mm a";

    private long tripDuration; //in seconds
    private LocalDateTime startTime; //example usage: LocalDateTime aDateTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
    private LocalDateTime stopTime;
    private Point.Float startPoint;
    private Point.Float endPoint;
    private int bikeId;
    private char gender; //u for unknown, m for male, f for female
    private int birthYear;
    private Double tripDistance;
    private String googleData;
    private boolean isUserDefinedPoint;


    /**
     * Constructor for a bike trip.
     * @param tripDuration duration (in seconds) of the bike trip
     * @param startTime datetime the bike trip started
     * @param stopTime datetime the bike trip ended
     * @param startPoint co-ordinates of the bike trip's origin
     * @param endPoint co-ordinates of the bike trip's terminus
     * @param bikeId the ID of the bike
     * @param gender the gender of the bike's rider (m, f, or u)
     * @param birthYear the year of birth of the rider
     * @param isUserDefinedPoint whether the point is user-defined or loaded from a CSV file/the
     *                           database TODO is this right?
     */
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender, int birthYear,
                    boolean isUserDefinedPoint) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = DataAnalyser.calculateDistance(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        this.isUserDefinedPoint = isUserDefinedPoint;
    }


    /**
     * Constructor for bike trip that calculates tripDuration
     * @param startTime datetime the bike trip started
     * @param stopTime datetime the bike trip ended
     * @param startPoint co-ordinates of the bike trip's origin
     * @param endPoint co-ordinates of the bike trip's terminus
     * @param bikeId the ID of the bike
     * @param gender the gender of the bike's rider (m, f, or u)
     * @param birthYear the year of birth of the rider
     * @param isUserDefinedPoint whether the point is user-defined or loaded from a CSV file/the
     *                           database TODO is this right?
     */
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime, Point.Float startPoint,
                    Point.Float endPoint, int bikeId, char gender, int birthYear, boolean isUserDefinedPoint) {
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = DataAnalyser.calculateDistance(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
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

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getBirthYear() {
        return this.birthYear;
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

    public Double getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(Double tripDistance) {
        this.tripDistance = tripDistance;
    }


    /**
     * Returns the duration of the trip, contextualised.
     * Either in seconds, minutes, hours, or days (rounded down).
     * This means that a trip that took 1 day, 23 hours, 59 minutes, 59 seconds will be "1 day".
     * E.g. "20 minutes", "3 days" (without the quotes).
     * @return the contextual duration as a string
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
     * @return the gender description (male, female, or unknown).
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


    /**
     * Returns the name of the bike trip (used for double-click popup).
     * E.g. "Trip at 10:20 am 15 March 2017"
     * @return bike trip name: "Trip at <start-time>"
     */
    public String getName() {
        String start = startTime.format(DateTimeFormatter.ofPattern(DT_FORMAT))
                .replace("AM", "am").replace("PM","pm");
        return String.format("Trip at %s", start);
    }


    /**
     * @return description of the bike trip.
     */
    public String getDescription() {
        String start = startTime.format(DateTimeFormatter.ofPattern(DT_FORMAT))
                .replace("AM", "am").replace("PM","pm");

        // end: check if including the date (or year) is necessary
        String end;
        if (startTime.getYear() != stopTime.getYear()) {
            // different years
            end = stopTime.format(DateTimeFormatter.ofPattern(DT_FORMAT));
        } else if (startTime.getDayOfYear() != stopTime.getDayOfYear()) {
            //different days
            end = stopTime.format(DateTimeFormatter.ofPattern(DAY_OF_MONTH_FORMAT));
        } else {
            //same day
            end = stopTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        }
        end = end.replace("AM", "am").replace("PM","pm");

        // Put together description
        return String.format("Started at %s and ended %s later at %s\nBike ID: %d\nCyclist: %s, born in %d",
                start, getDuration(), end, bikeId, getGenderDescription(), birthYear);
    }


    @Override
    public String toString() {
        return "BikeTrip{" +
                "tripDuration=" + tripDuration +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", bikeId=" + bikeId +
                ", gender=" + gender +
                ", birthYear=" + birthYear +
                ", tripDistance=" + tripDistance +
                ", googleData='" + googleData + '\'' +
                ", isUserDefinedPoint=" + isUserDefinedPoint +
                '}';
    }
}
