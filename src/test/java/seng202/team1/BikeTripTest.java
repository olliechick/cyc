package seng202.team1;

import junit.framework.TestCase;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;

public class BikeTripTest extends TestCase {

    public void testDurationlessConstructor(){
        LocalDateTime startTime =  LocalDateTime.of(2015, Month.JANUARY, 1,
                1, 1, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, Month.JANUARY, 1,
                1, 1, 10);
        Point.Float startPoint = new Point.Float(0, 0);
        Point.Float endPoint = new Point.Float(0, 1);
        int bikeID = 1;
        char gender = 'u';
        int birthYear = 2000;

        BikeTrip biketrip = new BikeTrip(startTime, stopTime, startPoint,
                endPoint, bikeID, gender, birthYear);
    }


}