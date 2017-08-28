package seng202.team1;

/***
 * Class for type of Wifi Points. Has a single constructor which sets all the values of the point.
 * CSV files contain more points but these are the important ones for the app.
 * All the methods in here are getters and setters (and toString).
 * @author Josh Burt
 * @author Ollie Chick
 */
public class WifiPoint extends DataPoint {

    private String objectId;
    private String the_geom;
    private String borough;
    private String cost;
    private String latitude;
    private String longitude;
    private String location; // not as helpful as it suggests, some are addresses some are not

    WifiPoint(String objectId, String the_geom, String borough, String cost, String latitude, String longitude, String location){
        this.objectId = objectId;
        this.the_geom = the_geom;
        this.borough = borough;
        this.cost = cost;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    public String getThe_geom() {
        return the_geom;
    }

    public void setThe_geom(String the_geom) {
        this.the_geom = the_geom;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "WifiPoint{" +
                "objectId='" + objectId + '\'' +
                ", the_geom='" + the_geom + '\'' +
                ", borough='" + borough + '\'' +
                ", cost='" + cost + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
