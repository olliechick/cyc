package seng202.team1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.LatLng;

import java.awt.*;
import java.io.IOException;

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
                        .await();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(result));
    }



    public static void main(String [ ] args) throws InterruptedException, ApiException, IOException {
       // googleGetDirections(40.745968480330795, -73.99403913047428, 40.745968480330795,-74.13915300041297);
        //example
        //System.out.println(googleGeocode("1600 Amphitheatre Parkway Mountain View, CA 94043"));


    }
}
