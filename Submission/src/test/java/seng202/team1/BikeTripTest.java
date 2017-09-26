package seng202.team1;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import seng202.team1.Model.BikeTrip;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertNotEquals;

public class BikeTripTest extends TestCase {

    long duration;
    LocalDateTime startTime;
    LocalDateTime stopTime;
    Point.Float startPoint;
    Point.Float endPoint;
    int startStationId;
    int endStationId;
    int bikeID;
    char gender;
    int birthYear;
    Double tripDistance;
    boolean isUserDefinedPoint;

        BikeTrip bikeTrip;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        duration = 10;
        startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        startStationId = 5;
        endStationId = 6;
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        tripDistance = Double.valueOf(12);
        isUserDefinedPoint = false;

        bikeTrip = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
    }

    @Test
    public void testGetDuration() {
        String expectedString = "10 seconds";
        assertEquals(expectedString, bikeTrip.getDuration());
    }

    @Test
    public void testDurationlessConstructor() {
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        assertEquals(10, biketrip1.getTripDuration());
    }

    @Test
    public void testGetGenderDescription() {
        assertEquals("female", bikeTrip.getGenderDescription());
    }

    @Test
    public void testGetName() {
        String expectedString = "Trip at 11:50 pm 30 December 2015";
        assertEquals(expectedString, bikeTrip.getName());
    }

    @Test
    public void testGetDescriptionSameDay() {
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 10 seconds later " +
                "at 11:50 pm\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip.getDescription());
    }

    @Test
    public void testGetDescriptionDifferentDay() {
        BikeTrip bikeTrip1 = new BikeTrip(startTime, stopTime.plusMinutes(20), startPoint,
                endPoint, startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 20 minutes later " +
                "at 12:10 am 31 December\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip1.getDescription());
    }

    @Test
    public void testGetDescriptionDifferentYear() {
        BikeTrip bikeTrip1 = new BikeTrip(startTime, stopTime.plusDays(1).plusMinutes(20),
                startPoint, endPoint, startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 1 day later at " +
                "12:10 am 1 January 2016\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip1.getDescription());
    }

    @Test
    public void testEqualsSame() {
        startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        isUserDefinedPoint = false;

        BikeTrip biketrip1 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        boolean isSame = biketrip1.equals(biketrip2);
        assertEquals(true, isSame);
    }

    @Test
    public void testHashCodesSame() {
        startTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        isUserDefinedPoint = false;

        BikeTrip biketrip1 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        int hash1 = biketrip1.hashCode();
        int hash2 = biketrip2.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testEqualsDifferent(){
        startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        isUserDefinedPoint = false;

        BikeTrip biketrip1 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startPoint = new Point.Float((float) 172.581154, (float) -43.522610);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        boolean isSame = biketrip1.equals(biketrip2);
        assertEquals(false, isSame);
    }

    @Test
    public void testHashCodesDifferent() {
        startTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        isUserDefinedPoint = false;

        BikeTrip biketrip1 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startPoint = new Point.Float((float) 172.581154, (float) -43.522610);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        boolean isSame = biketrip1.equals(biketrip2);

        int hash1 = biketrip1.hashCode();
        int hash2 = biketrip2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testEqualsEqualbutDifferent() {
        startTime =  LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        isUserDefinedPoint = false;


        BikeTrip biketrip1 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startTime = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 50, 0);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        boolean isSame = biketrip1.equals(biketrip2);
        assertEquals(true, isSame);
    }

    @Test
    public void testHashCodesEqualbutDifferent() {
        startTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 0);
        stopTime = LocalDateTime.of(2015, Month.DECEMBER, 30, 23, 50, 10);
        duration = 10;
        startPoint = new Point.Float((float) 172.581153, (float) -43.522610);
        endPoint = new Point.Float((float) 172.572739, (float) -43.520740);
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        isUserDefinedPoint = false;


        BikeTrip biketrip1 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startTime = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 50, 0);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                startStationId, endStationId, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        boolean isSame = biketrip1.equals(biketrip2);

        int hash1 = biketrip1.hashCode();
        int hash2 = biketrip2.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void TestEqualsNull() {
            boolean isSame = bikeTrip.equals(null);
            assertEquals(false, isSame);
    }

    @Test
    public void TestEqualsSameObj() {
            boolean isSame = bikeTrip.equals(bikeTrip);
            assertEquals(true, isSame);
    }

}