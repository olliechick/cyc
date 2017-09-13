package seng202.team1;

import java.awt.Point;
import java.time.LocalDateTime;

/***
 * Class for type of Wifi Points. Has a single constructor which sets all the values of the point.
 * CSV files contain more points but these are the important ones for the app.
 * All the methods in here are getters and setters (and toString).
 * @author Josh Burt
 * @author Ollie Chick
 */
public class WifiPoint extends DataPoint {

    private int objectId;
    private Point.Float coords;
    private String name;
    private String location;     // not as helpful as it suggests, some are addresses some are not
    private String locationType;
    private String hood;         // neighbourhood
    private String borough;
    private String city;
    private String zipcode;
    private String cost;         // e.g. Free, Limited Free
    private String provider;
    private String remarks;
    private String ssid;
    private String sourceId;
    private LocalDateTime datetimeActivated;

    public WifiPoint(int objectId, Point.Float coords, String name, String location, String locationType, String hood,
                     String borough, String city, String zipcode, String cost, String provider, String remarks, String ssid,
                     String sourceId, LocalDateTime datetimeActivated) {
        this.objectId = objectId;
        this.coords = coords;
        this.name = name;
        this.location = location;
        this.locationType = locationType;
        this.hood = hood;
        this.borough = borough;
        this.city = city;
        this.zipcode = zipcode;
        this.cost = cost;
        this.provider = provider;
        this.remarks = remarks;
        this.ssid = ssid;
        this.sourceId = sourceId;
        this.datetimeActivated = datetimeActivated;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
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

    @Override
    public String toString() {
        return "WifiPoint{" +
                "objectId=" + objectId +
                ", coords=" + coords +
                ", name='" + name + '\'' +
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
        return "Name: " + name + "\\n" +
                "Cost: " + cost + "\\n" +
                "Provider: " + provider + "\\n" +
                "SSID: " + ssid + "\\n" +
                "Since: " + datetimeActivated;

    }
}
