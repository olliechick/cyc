package seng202.team1;

import com.google.maps.errors.ApiException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class BikeDirections {

    private ArrayList<String> polylines;
    private LocalDate dateRetrieved;
    private Point.Double startPoint;
    private Point.Double endPoint;
    private ArrayList<Point.Double> points;
    private String duration; // human readable
    private String distance; // human readable



    /**
     * Constructor that parses a json and gets the polylines and the date retrieved (today's date).
     */
    public BikeDirections(String jsonString) throws InterruptedException, ApiException, IOException{

        JSONArray legs = new JSONObject(jsonString).getJSONArray("routes").getJSONObject(0)
                .getJSONArray("legs");
        JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

        polylines = new ArrayList<>();
        points = new ArrayList<>();
        for (int i = 0; i < steps.length(); i++) {
            //JSONObject JSONArray
            JSONObject step = steps.getJSONObject(i);
            String polyline = step.getJSONObject("polyline").getString("points");
            ArrayList<Point.Double>thesePoints = decodePoly(polyline);

            polylines.add(polyline);
            points.addAll(thesePoints);

            System.out.println(polyline);
            System.out.println(thesePoints);
        }

        dateRetrieved = LocalDate.now();
        startPoint = points.get(0);
        endPoint = points.get(points.size()-1);
        distance = legs.getJSONObject(0).getJSONObject("distance").getString("humanReadable");
        duration = legs.getJSONObject(0).getJSONObject("duration").getString("humanReadable");






//deal with multiple routes?
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
                ", dateRetrieved=" + dateRetrieved +
                '}';
    }
}