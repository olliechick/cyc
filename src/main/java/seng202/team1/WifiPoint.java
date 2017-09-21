package seng202.team1;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/***
 * Class for type of Wifi Points. Has a single constructor which sets all the values of the point.
 * CSV files contain more points but these are the important ones for the app.
 * All the methods in here are getters and setters (and toString).
 * @author Josh Burt
 * @author Ollie Chick
 */
public class WifiPoint extends DataPoint implements java.io.Serializable{

    private int objectId;
    private Point.Float coords;
    private String placeName;
    private String location;     // not as helpful as it suggests, some are addresses some are not
    private String locationType;
    private String hood;         // neighbourhood
    private String borough;
    private String city;
    private int zipcode;
    private String cost;         // e.g. Free, Limited Free
    private String provider;
    private String remarks;
    private String ssid;
    private String sourceId;
    private LocalDateTime datetimeActivated;
    private Double distanceFrom; // variable used for sorting the distance from a point

    public WifiPoint(int objectId, Point.Float coords, String placeName, String location, String locationType, String hood,
                     String borough, String city, int zipcode, String cost, String provider, String remarks, String ssid,
                     String sourceId, LocalDateTime datetimeActivated, boolean isUserDefinedPoint) {
        this.objectId = objectId;
        this.coords = coords;
        this.placeName = placeName;
        this.location = location;
        this.locationType = locationType;
        this.hood = hood;
        this.borough = borough;
        this.cost = cost;
        this.location = location;
        this.provider = provider;
        this.city = city;
        this.zipcode = zipcode;
        this.cost = cost;
        this.remarks = remarks;
        this.ssid = ssid;
        this.sourceId = sourceId;
        this.datetimeActivated = datetimeActivated;
        this.isUserDefinedPoint = isUserDefinedPoint;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public Point.Float getCoords() {
        return coords;
    }

    public void setCoords(Point.Float coords) {
        this.coords = coords;
    }

    public float getLongitude() {
        return coords.x;
    }

    public void setLongitude(float longitude) {
        this.coords.x = longitude;
    }

    public float getLatitude() {
        return coords.y;
    }

    public void setLatitude(float latitude) {
        this.coords.y = latitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getHood() {
        return hood;
    }

    public void setHood(String hood) {
        this.hood = hood;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public LocalDateTime getDatetimeActivated() {
        return datetimeActivated;
    }

    public void setDatetimeActivated(LocalDateTime datetimeActivated) {
        this.datetimeActivated = datetimeActivated;
    }

    public Double getDistanceFrom() {
        return distanceFrom;
    }

    public void setDistanceFrom(Double distanceFrom) {
        this.distanceFrom = distanceFrom;
    }

    /**
     * Returns the name of the WiFi point (SSID chosen as this is what will appear on the user's device
     * for them to connect to).
     */
    public String getName() {
        return ssid;
    }


    /**
     * Returns a description of the WiFi point.
     */
    public String getDescription() {
        return String.format("Location: %s (%s) - %s, %s, %s, %s %d (%f, %f)\nCost: %s\n" +
                "Provider: %s\nSSID: %s\nSourceID: %s\nActivated: %s\nID %d", location, locationType, placeName, hood, borough,
                city, zipcode, getLatitude(), getLongitude(), cost, provider, ssid, sourceId,
                datetimeActivated.format(DateTimeFormatter.ofPattern("h:mm:ss a d/M/yyyy")),
                objectId);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        WifiPoint that = (WifiPoint) obj;
        if (this.coords.equals(that.coords) && this.ssid.equalsIgnoreCase(that.ssid)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,31).
                append(coords).
                append(ssid).
                toHashCode();

    }


    @Override
    public String toString() {
        return "WifiPoint{" +
                "objectId=" + objectId +
                ", coords=" + coords +
                ", placeName='" + placeName + '\'' +
                ", location='" + location + '\'' +
                ", locationType='" + locationType + '\'' +
                ", hood='" + hood + '\'' +
                ", borough='" + borough + '\'' +
                ", city='" + city + '\'' +
                ", zipcode=" + zipcode +
                ", cost='" + cost + '\'' +
                ", provider='" + provider + '\'' +
                ", remarks='" + remarks + '\'' +
                ", ssid='" + ssid + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", datetimeActivated=" + datetimeActivated +
                '}';
    }

    public String toInfoString() {
        return "Name: " + placeName + "\\n" +
                "Cost: " + cost + "\\n" +
                "Provider: " + provider + "\\n" +
                "SSID: " + ssid + "\\n" +
                "Since: " + datetimeActivated;

    }
}
