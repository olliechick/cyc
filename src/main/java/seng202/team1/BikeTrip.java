package seng202.team1;

/**
 * Bike Trip data class. This will likely change during development as we need to do more./
 * So far it just builds a bike trip from CSV files
 * @author Josh Burt
 */
public class BikeTrip extends DataPoint {

    private String tripDuration;
    private String startTime;
    private String stopTime;
    private String startLongitude; //I've brought these in as strings but may need to make these Int tuples later
    private String startLatitude;
    private String endLongitude;
    private String endLatitude;


    public BikeTrip(String tripDuration, String startTime, String stopTime, String startLongitude, String startLatitude, String endLongitude, String endLatitude) {
        this.tripDuration = tripDuration;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
    }

    /**
     * Overides the equals method for bike trip and checks to see if a trip starts and ends at the same co-ordinates.
     * If the Coords match the two trips are equal.
     * @param b1
     * @param b2
     * @return boolean
     */
    //@Override
    public boolean equals(BikeTrip b1, BikeTrip b2){
        double b1StartLong = Double.parseDouble(b1.getStartLongitude());
        double b1StartLat = Double.parseDouble(b1.getStartLatitude());
        double b1EndLong = Double.parseDouble(b1.getEndLongitude());
        double b1EndLat = Double.parseDouble(b1.getEndLatitude());
        double b2StartLong = Double.parseDouble(b2.getStartLongitude());
        double b2StartLat = Double.parseDouble(b2.getStartLatitude());
        double b2EndLong = Double.parseDouble(b2.getEndLongitude());
        double b2EndLat = Double.parseDouble(b2.getEndLatitude());
        boolean StartsMatch = (b1StartLat == b2StartLat) && (b1StartLong == b2StartLong);
        boolean EndMatch = (b1EndLat == b2EndLat) && (b1EndLong == b2EndLong);
        if (StartsMatch && EndMatch){
            return true;
        } else {
            return false;
        }
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }



}
