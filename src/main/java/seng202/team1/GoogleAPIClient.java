package seng202.team1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static seng202.team1.CSVLoader.populateRetailers;

public class GoogleAPIClient {

    // non-verified google api key created by cameron. please dont waste.
    private static String KEY = "AIzaSyBrALsvaGRKhQxRw6X5VvydPqA5wMfwDN8";

    //note this method counts towards our daily limit for google api
    public static Point.Double googleGeocode(String address) throws InterruptedException, ApiException, IOException {
        Point.Double latLng = new Point.Double();
        GeoApiContext context = new GeoApiContext.Builder().apiKey(KEY).build();
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        latLng.setLocation(Double.parseDouble(gson.toJson(results[0].geometry.location.lat)), Double.parseDouble(gson.toJson(results[0].geometry.location.lng)));
        return latLng;
    }

    public static void getRetailerGeocode() {
        ArrayList<RetailerLocation> retailerPoints;
        try {
            retailerPoints = populateRetailers("src/main/resources/csv/Lower_Manhattan_Retailers.csv");
        } catch (CsvParserException|IOException e) {
            //TODO deal with the exception
            AlertGenerator.createAlert("Error", "Error generating retailers");
            return;
        }
        RetailerLocation point;
        String address;
        for (int i = 665; i < retailerPoints.size(); i++) {

            point = retailerPoints.get(i);
            address = point.getAddressLine1() + point.getAddressLine2() + ", " + point.getCity() + ", " + point.getZipcode();
            //System.out.println(address);
            try {
                Point.Double latlng = googleGeocode(address);
                System.out.println(latlng.getX() + ", " + latlng.getY());
            } catch (InterruptedException|ApiException|IOException e) {
                //e.printStackTrace();
            }

        }
    }

    // THIS IS A WORK IN PROGRESS
    public static void googleGetDirections(double latOrigin, double lngOrigin, double latDest, double lngDest) throws InterruptedException, ApiException, IOException {
        LatLng origin =  new LatLng(latOrigin, lngOrigin);
        LatLng destination = new LatLng(latDest, lngDest);
        GeoApiContext context = new GeoApiContext.Builder().apiKey(KEY).build();

        DirectionsResult result =
                DirectionsApi.newRequest(context)
                        .origin(origin)
                        .destination(destination)
                        .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
                        .mode(TravelMode.BICYCLING)
                        .alternatives(true)
                        .await();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(result));
    }



    public static void main(String [ ] args) throws InterruptedException, ApiException, IOException {
        googleGetDirections(40.745968480330795, -73.99403913047428, 40.745968480330795,-74.13915300041297);
        //example
        //System.out.println(googleGeocode("1600 Amphitheatre Parkway Mountain View, CA 94043"));
       // getRetailerGeocode();

    }
}
