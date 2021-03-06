package seng202.team1.Model;

import java.awt.Point;
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
    private int bikeId;
    private char gender; //u for unknown, m for male, f for female
    private int birthYear;
    private Double tripDistance; //in metres


    /**
     * Constructor for a bike trip.
     *
     * @param tripDuration duration (in seconds) of the bike trip
     * @param startTime    datetime the bike trip started
     * @param stopTime     datetime the bike trip ended
     * @param startPoint   co-ordinates of the bike trip's origin
     * @param endPoint     co-ordinates of the bike trip's terminus
     * @param bikeId       the ID of the bike
     * @param gender       the gender of the bike's rider (m, f, or u)
     * @param birthYear    the year of birth of the rider
     */
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender,
                    int birthYear, Double tripDistance) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikeId = bikeId;
        this.gender = gender;
        this.birthYear = birthYear;
        this.tripDistance = tripDistance;
    }


    /**
     * Constructor for a bike trip that calculates trip duration.
     *
     * @param startTime  datetime the bike trip started
     * @param stopTime   datetime the bike trip ended
     * @param startPoint co-ordinates of the bike trip's origin
     * @param endPoint   co-ordinates of the bike trip's terminus
     * @param bikeId     the ID of the bike
     * @param gender     the gender of the bike's rider (m, f, or u)
     * @param birthYear  the year of birth of the rider
     */
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender,
                    int birthYear, Double tripDistance) {
        this(0, startTime, stopTime, startPoint, endPoint, bikeId, gender, birthYear,
                tripDistance);
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
    }


    /**
     * Constructor for a bike trip that calculates trip distance.
     *
     * @param tripDuration duration (in seconds) of the bike trip
     * @param startTime    datetime the bike trip started
     * @param stopTime     datetime the bike trip ended
     * @param startPoint   co-ordinates of the bike trip's origin
     * @param endPoint     co-ordinates of the bike trip's terminus
     * @param bikeId       the ID of the bike
     * @param gender       the gender of the bike's rider (m, f, or u)
     * @param birthYear    the year of birth of the rider
     */
    public BikeTrip(long tripDuration, LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender,
                    int birthYear) {
        this(tripDuration, startTime, stopTime, startPoint, endPoint, bikeId, gender, birthYear,
                0.0);
        this.tripDistance = DataAnalyser.calculateDistance(startPoint.getX(), startPoint.getY(),
                endPoint.getX(), endPoint.getY());
    }


    /**
     * Constructor for a bike trip that calculates trip duration and distance.
     *
     * @param startTime  datetime the bike trip started
     * @param stopTime   datetime the bike trip ended
     * @param startPoint co-ordinates of the bike trip's origin
     * @param endPoint   co-ordinates of the bike trip's terminus
     * @param bikeId     the ID of the bike
     * @param gender     the gender of the bike's rider (m, f, or u)
     * @param birthYear  the year of birth of the rider
     */
    public BikeTrip(LocalDateTime startTime, LocalDateTime stopTime,
                    Point.Float startPoint, Point.Float endPoint, int bikeId, char gender,
                    int birthYear) {
        this(0, startTime, stopTime, startPoint, endPoint, bikeId, gender, birthYear,
                0.0);
        this.tripDuration = Duration.between(startTime, stopTime).getSeconds();
        this.tripDistance = DataAnalyser.calculateDistance(startPoint.getX(), startPoint.getY(),
                endPoint.getX(), endPoint.getY());
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


    /**
     * Used in the table view to display bike IDs, while treating -1 as null.
     * @return the bike ID of the retailer. If it is -1, then null is returned.
     */
    public String getBikeIdString() {
        if (bikeId == -1) {
            return null;
        } else {
            return "" + bikeId; //bikeId as a String
        }
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


    /**
     * Returns the duration of the trip, contextualised.
     * Either in seconds, minutes, hours, or days (rounded down).
     * This means that a trip that took 1 day, 23 hours, 59 minutes, 59 seconds will be "1 day".
     * E.g. "20 minutes", "3 days" (without the quotes).
     *
     * @return the contextual duration of the trip
     */
    public ContextualLength getDuration() {
        String durationString;

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

        durationString = unitCount + " " + unit;

        // then check if it's plural
        if (unitCount != 1) {
            durationString += "s";
        }

        return new ContextualLength(tripDuration, durationString);
    }


    /**
     * Returns the distance of the trip, contextualised.
     * Either in m (meters) or km (kilometres). Rounded to the nearest 3 digits of precision.
     * E.g. "236 m", "73.7 km" (without the quotes).
     * Currently coded for trips 1m - 999km.
     *
     * @return the contextualised distance of the trip
     */
    public ContextualLength getDistance() {
        String distanceString;

        if (tripDistance < 10) {
            //single digit metres (and below)
            distanceString = "" + Double.parseDouble(new DecimalFormat("#.##").format(tripDistance));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " m";
        } else if (tripDistance < 100) {
            //double digit metres
            distanceString = "" + Double.parseDouble(new DecimalFormat("##.#").format(tripDistance));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " m";
        } else if (tripDistance < 1e3) {
            //three digit metres
            distanceString = Math.round(tripDistance) + " m";
        } else if (tripDistance < 1e4) {
            //single digit km
            distanceString = "" + Double.parseDouble(new DecimalFormat("#.##").format(tripDistance / 1e3));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " km";
        } else if (tripDistance < 1e5) {
            //double digit km
            distanceString = "" + Double.parseDouble(new DecimalFormat("##.#").format(tripDistance / 1e3));
            if (distanceString.endsWith(".0")) {
                // Trim the .0
                distanceString = distanceString.substring(0, distanceString.length() - 2);
            }
            distanceString += " km";
        } else {
            //triple digit km (and above)
            distanceString = Math.round(tripDistance / 1e3) + " km";
        }

        return new ContextualLength(tripDistance, distanceString);
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
        return "Trip at " + startTime.format(DateTimeFormatter.ofPattern(DT_FORMAT))
                .replace("AM", "am").replace("PM", "pm");
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
            // We know the gender
            cyclistDescription = "\nCyclist: " + getGenderDescription();
            if (!(birthYear == -1)) {
                // Know both things about cyclist
                cyclistDescription += ", born in " + birthYear;
            }
        }

        // Put together description
        return "Started at " + start + " and ended " + getDuration() + " later at " + end + coords + distance + bikeIdString + cyclistDescription;
    }

    public void setAllProperties(BikeTrip newBikeTrip) {
        this.tripDuration = Duration.between(newBikeTrip.getStartTime(), newBikeTrip.getStopTime()).getSeconds();
        this.startTime = newBikeTrip.getStartTime();
        this.stopTime = newBikeTrip.getStopTime();
        this.startPoint = newBikeTrip.getStartPoint();
        this.endPoint = newBikeTrip.getEndPoint();
        this.bikeId = newBikeTrip.getBikeId();
        this.gender = newBikeTrip.getGender();
        this.birthYear = newBikeTrip.getBirthYear();
        this.tripDistance = DataAnalyser.calculateDistance(newBikeTrip.getStartPoint().getX(), newBikeTrip.getStartPoint().getY(),
                newBikeTrip.getEndPoint().getX(), newBikeTrip.getEndPoint().getY());
    }

    /**
     * @return A nicer description of the trip for the map
     */
    public String nicerDescription() {
        return "This trip took " + getDuration() +
                " and ends " + getDistance() + " away at (" + getEndLatitude() + ", " + getEndLongitude() + ").";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BikeTrip bikeTrip = (BikeTrip) o;

        if (bikeId != bikeTrip.bikeId) return false;
        if (!startTime.equals(bikeTrip.startTime)) return false;
        if (!stopTime.equals(bikeTrip.stopTime)) return false;
        if (!startPoint.equals(bikeTrip.startPoint)) return false;
        return endPoint.equals(bikeTrip.endPoint);
    }

    @Override
    public int hashCode() {
        int result = startTime.hashCode();
        result = 31 * result + stopTime.hashCode();
        result = 31 * result + startPoint.hashCode();
        result = 31 * result + endPoint.hashCode();
        result = 31 * result + bikeId;
        return result;
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
                '}';
    }
}
