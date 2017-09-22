package seng202.team1;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class to implement routing methods
 * @author Josh Burt
 */
public final class Routing {

    /**
     * Class for finding common bike routes from a point.
     * Takes a list of bike trips and a point and returns the first 10 trips
     * found for that point or all trips found within 1000m if less than 10 trips exist.
     * @param ourPoint Point to test from
     * @param triplist list to test against
     * @return list containing all trips meeting the criteria
     */
    public static ArrayList<BikeTrip> findCommonTripsFromPosition(Point.Float ourPoint, ArrayList<BikeTrip> triplist){
        ArrayList<BikeTrip> results = new ArrayList<>();
        int delta = 100; // allows us to increment the search radius
        while(results.size() < 9 && delta < 1001){
            ArrayList<BikeTrip> holder = DataAnalyser.searchBikeTrips(ourPoint.getY(), ourPoint.getX(), delta, triplist);
            for (BikeTrip t : holder){
                results.add(t);
            }
            delta += 100;

        }
        return results;
    }

    /**
     * Takes a point a list of biketrips and a requested duration.
     * this then returns either the first 10 bike trips within 10 mins of the duration
     * sorted by duration or all the bike trips within 20 mins of the duration sorted by time.
     * @param ourPoint Point to start from
     * @param tripList biketrips to compare against
     * @param duration specified time durration
     * @return a list of points meting the criteria sorted.
     */
    public static ArrayList<BikeTrip> findSightSeeingRoutes(Point.Float ourPoint, ArrayList<BikeTrip> tripList, long duration) {
        ArrayList<BikeTrip> resultsNonTime = new ArrayList<>();
        ArrayList<BikeTrip> resultsCorrectTime = new ArrayList<>();
        int delta = 100; // allows us to increment the search radius
        while (resultsCorrectTime.size() < 9 && delta < 1001) {
            ArrayList<BikeTrip> holder = DataAnalyser.searchBikeTrips(ourPoint.getY(), ourPoint.getX(), delta, tripList);
            for (BikeTrip t : holder) {
                if ((Math.abs(t.getTripDuration() - duration) < 10)) {
                    resultsCorrectTime.add(t);
                } else if (((Math.abs(t.getTripDuration() - duration) < 20))) {
                    resultsNonTime.add(t);
                }
            }
            delta += 100;

        }
        if (resultsCorrectTime.size() >= 1) {
            Collections.sort(resultsCorrectTime, new Comparator<BikeTrip>() {
                @Override
                public int compare(BikeTrip o1, BikeTrip o2) {
                    return (int) (o1.getTripDuration() - o2.getTripDuration());
                }
            });

            return resultsCorrectTime;
        } else {
            Collections.sort(resultsNonTime, new Comparator<BikeTrip>() {
                @Override
                public int compare(BikeTrip o1, BikeTrip o2) {
                    return (int) (o1.getTripDuration() - o2.getTripDuration());
                }
            });
            return resultsNonTime;
        }
    }
}
