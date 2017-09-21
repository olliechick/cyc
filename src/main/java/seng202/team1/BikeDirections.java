package seng202.team1;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BikeDirections {

    private ArrayList<String> overviewPolylines;
    private LocalDate dateRetrieved;


    /**
     * Default constructor for BikeDirections
     */
    public BikeDirections(ArrayList<String> overviewPolyline, LocalDate dateRetrieved) {
        this.overviewPolylines = overviewPolyline;
        this.dateRetrieved = dateRetrieved;
    }

    /**
     * Constructor that parses a gson and gets the polylines and the date retrieved (today's date).
     */
    public BikeDirections(String jsonFile) {
        //JsonReader jsonReader = Json.createReader();
        //JsonObject object = jsonReader.readObject();
        //jsonReader.close();



//deal with multiple routes?
    }


    public ArrayList<String> getOverviewPolyline() {
        return overviewPolylines;
    }

    public void setOverviewPolyline(ArrayList<String> overviewPolyline) {
        this.overviewPolylines = overviewPolyline;
    }

    public LocalDate getDateRetrieved() {
        return dateRetrieved;
    }

    public void setDateRetrieved(LocalDate dateRetrieved) {
        this.dateRetrieved = dateRetrieved;
    }



    // from http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
    private static ArrayList<Point.Double> decodePoly(String encoded) {

        ArrayList<Point.Double> poly = new ArrayList<Point.Double>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            Point.Double p = new Point.Double(lng / 1e5, lat / 1e5);
            poly.add(p);
        }

        return poly;
    }


    @Override
    public String toString() {
        return "BikeDirections{" +
                "overviewPolylines='" + overviewPolylines + '\'' +
                ", dateRetrieved=" + dateRetrieved +
                '}';
    }
}