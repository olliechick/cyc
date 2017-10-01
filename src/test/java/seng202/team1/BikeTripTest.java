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
        bikeID = 1;
        gender = 'f';
        birthYear = 2000;
        tripDistance = 12.0;
        isUserDefinedPoint = false;

        bikeTrip = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
    }

    @Test
    public void testGetDuration() {
        String expectedString = "10 seconds";
        assertEquals(expectedString, bikeTrip.getDuration().toString());
    }

    @Test
    public void testDurationlessConstructor() {
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        assertEquals(10, biketrip1.getTripDuration());
    }

    public void testGetDistance1m() {
        Double distance = 1.23456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("1.23 m", biketrip1.getDistance().toString());
    }

    public void testGetDistance10m() {
        Double distance = 12.3456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("12.3 m", biketrip1.getDistance().toString());
    }

    public void testGetDistance100m() {
        Double distance = 123.456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("123 m", biketrip1.getDistance().toString());
    }

    public void testGetDistance1km() {
        Double distance = 1234.56789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("1.23 km", biketrip1.getDistance().toString());
    }

    public void testGetDistance10km() {
        Double distance = 12345.6789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("12.3 km", biketrip1.getDistance().toString());
    }

    public void testGetDistanceLong() {
        Double distance = 123456.789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("123 km", biketrip1.getDistance().toString());
    }

    public void testGetDistance1mRoundUp() {
        Double distance = 1.239456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("1.24 m", biketrip1.getDistance().toString());
    }

    public void testGetDistance10mRoundUp() {
        Double distance = 12.39456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("12.4 m", biketrip1.getDistance().toString());
    }

    public void testGetDistance100mRoundUp() {
        Double distance = 123.9456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
               bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("124 m", biketrip1.getDistance().toString());
    }

    public void testGetDistance1kmRoundUp() {
        Double distance = 1239.456789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("1.24 km", biketrip1.getDistance().toString());
    }

    public void testGetDistance10kmRoundUp() {
        Double distance = 12394.56789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("12.4 km", biketrip1.getDistance().toString());
    }

    public void testGetDistanceLongRoundUp() {
        Double distance = 123945.6789;
        BikeTrip biketrip1 = new BikeTrip(startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
                birthYear, distance, isUserDefinedPoint);
        assertEquals("124 km", biketrip1.getDistance().toString());
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
                "at 11:50 pm\nFrom: (-43.52261, 172.58115)\n" +
                "To: (-43.52074, 172.57274)\n" +
                "Distance: 12.0 m\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip.getDescription());
    }

    @Test
    public void testGetDescriptionDifferentDay() {
        BikeTrip bikeTrip1 = new BikeTrip(startTime, stopTime.plusMinutes(20), startPoint,
                endPoint, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 20 minutes later " +
                "at 12:10 am 31 December\nFrom: (-43.52261, 172.58115)\n" +
                "To: (-43.52074, 172.57274)\n" +
                "Distance: 12.0 m\nBike ID: 1\nCyclist: female, born in 2000";
        assertEquals(expectedString, bikeTrip1.getDescription());
    }

    @Test
    public void testGetDescriptionDifferentYear() {
        BikeTrip bikeTrip1 = new BikeTrip(startTime, stopTime.plusDays(1).plusMinutes(20),
                startPoint, endPoint, bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        String expectedString = "Started at 11:50 pm 30 December 2015 and ended 1 day later at " +
                "12:10 am 1 January 2016\nFrom: (-43.52261, 172.58115)\n" +
                "To: (-43.52074, 172.57274)\n" +
                "Distance: 12.0 m\nBike ID: 1\nCyclist: female, born in 2000";
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
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
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
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
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
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startPoint = new Point.Float((float) 172.581154, (float) -43.522610);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
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
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startPoint = new Point.Float((float) 172.581154, (float) -43.522610);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
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
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startTime = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 50, 0);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
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
                bikeID, gender,
                birthYear, tripDistance, isUserDefinedPoint);

        startTime = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 50, 0);
        BikeTrip biketrip2 = new BikeTrip(duration, startTime, stopTime, startPoint, endPoint,
                bikeID, gender,
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