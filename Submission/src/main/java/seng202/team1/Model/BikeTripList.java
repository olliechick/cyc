package seng202.team1.Model;

import java.util.ArrayList;

/**
 * Created on 3/10/17.
 *
 * Data structure for containing a list of points and a name
 *
 * @author Josh Bernasconi
 */
public class BikeTripList extends PointList implements java.io.Serializable{

    private ArrayList<BikeTrip> bikeTrips = new ArrayList<>();

    public BikeTripList(String listName, ArrayList<BikeTrip> trips) {
        super(listName);
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

}
