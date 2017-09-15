package seng202.team1;

import junit.framework.TestCase;

import java.awt.Point;

public class RetailerLocationTest extends TestCase {

    String name;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    int zipcode;
    String blockLot;
    String primaryFunction;
    String secondaryFunction;
    Point.Float coords;
    RetailerLocation retailerLocation;
    boolean isUserDefinedPoint;

    public void setUp() throws Exception {
        super.setUp();
        name = "Pearl Bodywork";
        addressLine1 = "60 Pearl Street";
        addressLine2 = "Floor 2";
        city = "New York";
        state = "NY";
        zipcode = 10004;
        blockLot = "7-38";
        primaryFunction = "Personal and Professional Services";
        secondaryFunction = "Spa";
        coords = new Point.Float((float) -74.011071, (float) 40.703417);
        retailerLocation = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
    }

    public void testGetAddress() {
        String expectedAddress = "Floor 2, 60 Pearl Street, New York, NY 10004";
        assertEquals(expectedAddress, retailerLocation.getAddress());
    }

    public void testGetAddressNoAddressLine2() {
        addressLine2 = "";
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2,
                city, state, zipcode, blockLot, primaryFunction, secondaryFunction, isUserDefinedPoint);
        String expectedAddress = "60 Pearl Street, New York, NY 10004";
        assertEquals(expectedAddress, retailerLocation1.getAddress());
    }

    public void testCoordlessConstructor() {
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2,
                city, state, zipcode, blockLot, primaryFunction, secondaryFunction, isUserDefinedPoint);
        assertEquals(null, retailerLocation1.getCoords());
    }

    public void testGetDescription() {
        String expectedString = "Address: Floor 2, 60 Pearl Street, New York, NY 10004. Function: Personal and " +
                "Professional Services (Spa).";
        assertEquals(expectedString, retailerLocation.getDescription());
    }


}