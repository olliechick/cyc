package seng202.team1.Model;

import java.util.ArrayList;

/**
 * Created by jbe113 on 3/10/17.
 */
public class BikeTripList implements java.io.Serializable{

    private ArrayList<BikeTrip> bikeTrips = new ArrayList<>();
    private String listName;

    public BikeTripList(String listName, ArrayList<BikeTrip> trips) {
        this.listName = listName;
        this.bikeTrips = trips;
    }


    public ArrayList<BikeTrip> getBikeTrips() {
        return bikeTrips;
    }

    public void addBikeTrip(BikeTrip bikeTrip) {
        bikeTrips.add(bikeTrip);
    }

    public void removeBikeTrip(BikeTrip bikeTrip) {
        bikeTrips.remove(bikeTrip);
    }

    public String getListName() {
        return listName;
    }

}
