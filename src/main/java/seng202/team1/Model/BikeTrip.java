package seng202.team1.Model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Bike Trip data class.
 * If birthYear = -1, this is a flag which means the data is not available.
 * Gender will be m (male), f (female), or u (unknown).
 *
 * @author Josh Burt
 * @author Ollie Chick
 */
public class BikeTrip extends DataPoint implements java.io.Serializable {

    private final static String TIME_FORMAT = "h:mm a";
    private final static String DAY_OF_MONTH_FORMAT = TIME_FORMAT + " d MMMM";
    private final static String DT_FORMAT = DAY_OF_MONTH_FORMAT + " yyyy";

    private long tripDuration; //in seconds
    private LocalDateTime startTime;
    private LocalDateTime stopTime;
    private Point.Float startPoint;
    private Point.Float endPoint;
    private int startStationId;
    private int endStationId;
    private int bikeId;
    private char gender; //u for unknown, m for male, f for female
    private int birthYear;
    private Double tripDistance; //in metres
    private boolean isUserDefinedPoint;


    /**
     * Constructor for a bike trip.
     *
     * @param tripDuration       duration (in seconds) of the bike trip
     * @param startTime          datetime the bike trip started
     * @param stopTime           datetime the bike trip ended
     * @param startPoint         co-ordinates of the bike trip's origin
     * @param endPoint           co-ordinates of the bike trip's terminus
     * @param startStationId     station ID of the start point. Null if didn't start at a station
     * @param endStationId       station ID of the end point. Null if didn't end at a station
     * @param bikeId             the ID of the bike
     * @param gender             the gender of the bike's rider (m, f, or u)
     * @param birthYear          the year of birth of the rider
     * @param isUserDefinedPoint whether the point is user-defined or loaded from file
     */
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int startStationId,
                    int endStationId, int bikeId, char gender, int birthYear, Double tripDistance,
                    boolean isUserDefinedPoint) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = tripDistance;
        this.isUserDefinedPoint = isUserDefinedPoint;
    }


    /**
     * Constructor for a bike trip that calculates tripDuration.
     *
     * @param startTime          datetime the bike trip started
     * @param stopTime           datetime the bike trip ended
     * @param startPoint         co-ordinates of the bike trip's origin
     * @param endPoint           co-ordinates of the bike trip's terminus
     * @param startStationId     station ID of the start point. -1 flag if didn't start at a station
     * @param endStationId       station ID of the end point. -1 flag if didn't end at a station
     * @param bikeId             the ID of the bike
     * @param gender             the gender of the bike's rider (m, f, or u)
     * @param birthYear          the year of birth of the rider
     * @param isUserDefinedPoint whether the point is user-defined or loaded from file
     */
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int startStationId,
                    int endStationId, int bikeId, char gender, int birthYear, Double tripDistance,
                    boolean isUserDefinedPoint) {
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = tripDistance;
        this.isUserDefinedPoint = isUserDefinedPoint;
    }


    /**
     * Grandfathered-in constructor for a bike trip that calculates trip distance.
     * Start and end station IDs are set to -1 flag.
     *
     * @param tripDuration       duration (in seconds) of the bike trip
     * @param startTime          datetime the bike trip started
     * @param stopTime           datetime the bike trip ended
     * @param startPoint         co-ordinates of the bike trip's origin
     * @param endPoint           co-ordinates of the bike trip's terminus
     * @param bikeId             the ID of the bike
     * @param gender             the gender of the bike's rider (m, f, or u)
     * @param birthYear          the year of birth of the rider
     * @param isUserDefinedPoint whether the point is user-defined or loaded from file
     */
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender,
                    int birthYear, boolean isUserDefinedPoint) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startStationId = -1;
        this.endStationId = -1;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = DataAnalyser.calculateDistance(startPoint.getX(), startPoint.getY(),
                endPoint.getX(), endPoint.getY());
        this.isUserDefinedPoint = isUserDefinedPoint;
    }


    /**
     * Grandfathered-in constructor for a bike trip that calculates trip duration and distance.
     * Start and end station IDs are set to -1 flag.
     *
     * @param startTime          datetime the bike trip started
     * @param stopTime           datetime the bike trip ended
     * @param startPoint         co-ordinates of the bike trip's origin
     * @param endPoint           co-ordinates of the bike trip's terminus
     * @param bikeId             the ID of the bike
     * @param gender             the gender of the bike's rider (m, f, or u)
     * @param birthYear          the year of birth of the rider
     * @param isUserDefinedPoint whether the point is user-defined or loaded from file
     */
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender,
                    int birthYear, boolean isUserDefinedPoint) {
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startStationId = -1;
        this.endStationId = -1;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = DataAnalyser.calculateDistance(startPoint.getX(), startPoint.getY(),
                endPoint.getX(), endPoint.getY());
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

    public Double getTripDistance() {
        return tripDistance;
    }

    /**
     * @return trip distance to decimal places
     */
    public Double getTripDistanceTwoD() {
        return Double.parseDouble(new DecimalFormat("#.##").format(tripDistance));
    }

    public void setTripDistance(Double tripDistance) {
        this.tripDistance = tripDistance;
    }

    public int getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(int startStationId) {
        this.startStationId = startStationId;
    }

    public int getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(int endStationId) {
        this.endStationId = endStationId;
    }


    /**
     * Returns the duration of the trip, contextualised.
     * Either in seconds, minutes, hours, or days (rounded down).
     * This means that a trip that took 1 day, 23 hours, 59 minutes, 59 seconds will be "1 day".
     * E.g. "20 minutes", "3 days" (without the quotes).
     *
     * @return the contextual duration as a string
     */
    public String getDuration() {
        String duration;

        // first find the unit of time and how many of those units
        String unit;
        long unitCount;
        if (tripDuration < 60) {
            unit = "second";
            unitCount = tripDuration;
        } else if (tripDuration < 60 * 60) {
            unit = "minute";
            unitCount = tripDuration / 60;
        } else if (tripDuration < 60 * 60 * 24) {
            unit = "hour";
            unitCount = tripDuration / (60 * 60);
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
     * Returns the distance of the trip, contextualised.
     * Either in m (meters) or km (kilometres). Rounded to the nearest 3 digits of precision.
     * E.g. "236 m", "73.7 km" (without the quotes).
     * @return contextualised distance
     */
    public String getDistance() {
        String distance;
        System.out.println();

        if (tripDistance < 10) {
            //single digit metres
            return Double.parseDouble(new DecimalFormat("#.##").format(tripDistance)) + " m";
        } else if (tripDistance < 100) {
            //double digit metres
            return Double.parseDouble(new DecimalFormat("##.#").format(tripDistance)) + " m";
        } else if (tripDistance < 1e3) {
            //three digit metres
            return Math.round(tripDistance) + " m";
        } else if (tripDistance < 1e4) {
            //single digit km
            return Double.parseDouble(new DecimalFormat("#.##").format(tripDistance/1e3)) + " km";
        } else if (tripDistance < 1e5) {
            //double digit km
            return Double.parseDouble(new DecimalFormat("##.#").format(tripDistance/1e3)) + " km";
        } else {
            return Math.round(tripDistance/1e3) + " km";
        }
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
     *
     * @return bike trip name: "Trip at <start-time>"
     */
    public String getName() {
        String start = startTime.format(DateTimeFormatter.ofPattern(DT_FORMAT))
                .replace("AM", "am").replace("PM", "pm");
        return String.format("Trip at %s", start);
    }


    /**
     * @return description of the bike trip.
     */
    public String getDescription() {
        // Start
        String start = startTime.format(DateTimeFormatter.ofPattern(DT_FORMAT))
                .replace("AM", "am").replace("PM", "pm");

        // End: check if including the date (or year) is necessary
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
        end = end.replace("AM", "am").replace("PM", "pm");

        // Coords and distance
        String coords = "\nFrom: (" + getStartLatitude() + ", " + getStartLongitude() + ")"
                + "\nTo: (" + getEndLatitude() + ", " + getEndLongitude() + ")";
        String distance = "\nDistance: " + getDistance();

        // Bike ID
        String bikeIdString = "";
        if (bikeId != -1) {
            bikeIdString = "\nBike ID: " + bikeId;
        }

        // Cyclist description
        String cyclistDescription;
        if (gender == 'u') {
            if (birthYear == -1) {
                // Nothing known about cyclist
                cyclistDescription = "";
            } else {
                // Just know birth year
                cyclistDescription = "\nCyclist: born in " + birthYear;
            }
        } else {
            if (birthYear == -1) {
                // Just know gender
                cyclistDescription = "\nCyclist: " + getGenderDescription();
            } else {
                // Know both things about cyclist
                cyclistDescription = "\nCyclist: " + getGenderDescription() + ", born in " + birthYear;
            }
        }

        // Put together description
        return "Started at " + start + " and ended " + getDuration() + " later at " + end + coords + distance + bikeIdString + cyclistDescription;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) { // same obj is passed
            return true;
        }
        BikeTrip that = (BikeTrip) obj;

        return this.getStartPoint().equals(that.getStartPoint())
                && this.getEndPoint().equals(that.getEndPoint());
    }


    @Override
    public int hashCode() { // must override hashcode when overriding equality
        return new HashCodeBuilder(17, 31)
                .append(startPoint)
                .append(endPoint)
                .toHashCode();

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
                ", isUserDefinedPoint=" + isUserDefinedPoint +
                '}';
    }
}
