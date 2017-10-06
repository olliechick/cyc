package seng202.team1.Model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;

/***
 * Class for type of Wifi Points. Has a single constructor which sets all the values of the point.
 * CSV files contain more points but these are the important ones for the app.
 * All the methods in here are getters and setters (and toString).
 * @author Josh Burt
 * @author Ollie Chick
 */
public class WifiPoint extends DataPoint implements java.io.Serializable {

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

    /**
     * Constructor for WifiPoint
     *
     * @param objectId           ID of object
     * @param coords             co-ordinates of WiFi
     * @param placeName          name of the place where the WiFi is - e.g. Wingate Park
     * @param location           location of WiFi, usually an address
     * @param locationType       type of location - e.g. Outdoor Kiosk
     * @param hood               neighbourhood where WiFi is - e.g. Chinatown
     * @param borough            borough where WiFi is
     * @param city               city where WiFi is
     * @param zipcode            ZIP code of where WiFi is
     * @param cost               cost of WiFi (Free, Limited Free, or Partner Site)
     * @param provider           provider of WiFi
     * @param remarks            any remarks about the WiFi
     * @param ssid               SSID of WiFi - this is the "name" of the WiFi a user sees on their device
     * @param sourceId           Source ID of WiFi
     * @param datetimeActivated  date and time the WiFi was activated
     */
    public WifiPoint(int objectId, Point.Float coords, String placeName, String location, String locationType, String hood,
                     String borough, String city, int zipcode, String cost, String provider, String remarks, String ssid,
                     String sourceId, LocalDateTime datetimeActivated) {
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
     * for them to connect to). If the point is user defined, this will be appended by " (user-defined)".
     */
    public String getName() {
        return ssid;
    }


    /**
     * @return description of the WiFi point.
     */
    public String getDescription() {
        String description = "Location:";

        //First bit of location
        if (location != null && !location.isEmpty()) {
            // Location is defined
            description += " " + location;
            if (locationType != null && !locationType.isEmpty()) {
                //Location type is defined
                description += " (" + locationType + ") -";
            } else {
                // Location type is empty
                description += " -";
            }
        } else {
            // Location is empty
            if (location != null && !locationType.isEmpty()) {
                // Location type is defined
                description += " " + locationType + " -";
            }
        }

        // Second bit of location
        if (placeName != null && !placeName.isEmpty()) {
            description += " " + placeName + ",";
        }
        description += " " + hood + ", " + borough + ", " + city + " " + zipcode;
        description += "\nCoordinates: (" + getLatitude() + ", " + getLongitude() + ")";

        // The rest of description
        if (provider != null && !provider.isEmpty()) {
            description += "\nProvider: " + provider;
        }
        description += "\nCost: " + cost;
        if (sourceId != null && !sourceId.isEmpty()) {
            description += "\nSource ID: " + sourceId;
        }
        if (datetimeActivated != null) {
            String activatedString = "\nActivated: " + datetimeActivated.format(DateTimeFormatter.ofPattern("h:mm:ss a d/M/yyyy"));
            description += activatedString.replace("AM", "am").replace("PM", "pm");
        }
        if (objectId != -1) {
            description += "\nID: " + objectId;
        }
        if (remarks != null && !remarks.isEmpty()) {
            description += "\nRemarks: " + remarks;
        }

        return description;
    }

    public void setAllProperties(WifiPoint wifiPoint) {
        this.objectId = wifiPoint.getObjectId();
        this.coords = wifiPoint.getCoords();
        this.placeName = wifiPoint.getPlaceName();
        this.location = wifiPoint.getLocation();
        this.locationType = wifiPoint.getLocationType();
        this.hood = wifiPoint.getHood();
        this.borough = wifiPoint.getBorough();
        this.cost = wifiPoint.getCost();
        this.location = wifiPoint.getLocation();
        this.provider = wifiPoint.getProvider();
        this.city = wifiPoint.getCity();
        this.zipcode = wifiPoint.getZipcode();
        this.cost = wifiPoint.getCost();
        this.remarks = wifiPoint.getRemarks();
        this.ssid = wifiPoint.getSsid();
        this.sourceId = wifiPoint.getSourceId();
        this.datetimeActivated = wifiPoint.getDatetimeActivated();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        WifiPoint that = (WifiPoint) obj;
        return this.coords.equals(that.coords) && this.ssid.equalsIgnoreCase(that.ssid);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
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
                ", distanceFrom=" + distanceFrom +
                '}';
    }

    public String toInfoString() {
        return escapeEcmaScript(getName() + "\n" + getDescription());

    }
}
