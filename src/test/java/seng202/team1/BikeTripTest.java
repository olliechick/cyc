package seng202.team1;

import junit.framework.TestCase;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;

public class BikeTripTest extends TestCase {

    LocalDateTime startTime;
    LocalDateTime stopTime;
    int duration;
    Point.Float startPoint;
    Point.Float endPoint;
    int bikeID;
    char gender;
    int birthYear;
    BikeTrip bikeTrip;


    public void setUp() throws Exception {
        super.setUp();
        startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30,23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;

        bikeTrip = new BikeTrip(duration, startTime, stopTime, startPoint,
                endPoint, bikeID, gender, birthYear);
    }


    public void testDurationlessConstructor(){
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint, bikeID,
                gender, birthYear);
        assertEquals(10, biketrip1.getTripDuration());
    }


    public void testGetDescriptionSameDay() {
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 10 seconds later " +
                "at 11:50 pm\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip.getDescription());
    }


    public void testGetDescriptionDifferentDay() {
        BikeTrip bikeTrip1 = new BikeTrip(startTime, stopTime.plusMinutes(20), startPoint,
                endPoint, bikeID, gender, birthYear);
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 20 minutes later " +
                "at 12:10 am 31 December\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip1.getDescription());
    }


    public void testGetDescriptionDifferentYear() {
        BikeTrip bikeTrip1 = new BikeTrip(startTime, stopTime.plusDays(1).plusMinutes(20),
                startPoint, endPoint, bikeID, gender, birthYear);
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 1 day later at " +
                "12:10 am 1 January 2016\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip1.getDescription());
    }


}