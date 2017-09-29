package seng202.team1;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import seng202.team1.Model.RetailerLocation;

import java.awt.Point;

import static org.junit.Assert.assertNotEquals;

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
    boolean isUserDefinedPoint;

    RetailerLocation retailerLocation;

    @Before
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

    @Test
    public void testGetAddress() {
        String expectedAddress = "Floor 2, 60 Pearl Street, New York, NY 10004";
        assertEquals(expectedAddress, retailerLocation.getAddress());
    }

    @Test
    public void testGetAddressNoAddressLine2() {
        addressLine2 = "";
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2,
                city, state, zipcode, blockLot, primaryFunction, secondaryFunction, isUserDefinedPoint);
        String expectedAddress = "60 Pearl Street, New York, NY 10004";
        assertEquals(expectedAddress, retailerLocation1.getAddress());
    }

    @Test
    public void testCoordlessConstructor() {
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2,
                city, state, zipcode, blockLot, primaryFunction, secondaryFunction, isUserDefinedPoint);
        assertEquals(null, retailerLocation1.getCoords());
    }

    @Test
    public void testGetDescription() {
        String expectedString = "Address: Floor 2, 60 Pearl Street, New York, NY 10004\n" +
                "Block-Lot: 7-38\n" +
                "Co-ordinates: (40.70342, -74.01107)\n" +
                "Function: Personal and Professional Services (Spa)";
        assertEquals(expectedString, retailerLocation.getDescription());
    }

    @Test
    public void testEqualsSame(){
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
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        boolean isSame = retailerLocation.equals(retailerLocation1);
        assertEquals(true, isSame);
    }

    @Test
    public void testEqualsSameButDifferent(){
        name = "Pearl Bodywork";
        addressLine1 = "60 Pearl Street";
        addressLine2 = "Floor 3";
        city = "New York";
        state = "NY";
        zipcode = 10004;
        blockLot = "7-38";
        primaryFunction = "Personal and Professional Services";
        secondaryFunction = "Spa";
        coords = new Point.Float((float) -74.011071, (float) 40.703417);
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        boolean isSame = retailerLocation.equals(retailerLocation1);
        assertEquals(true, isSame);
    }

    @Test
    public void testEqualsDifferent(){
        name = "Pearl Bodywor";
        addressLine1 = "60 Pearl Street";
        addressLine2 = "Floor 2";
        city = "New York";
        state = "NY";
        zipcode = 10004;
        blockLot = "7-38";
        primaryFunction = "Personal and Professional Services";
        secondaryFunction = "Spa";
        coords = new Point.Float((float) -74.011171, (float) 40.703417);
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        boolean isSame = retailerLocation.equals(retailerLocation1);
        assertEquals(false, isSame);
    }

    @Test
    public void testEqualsNull(){
        assertEquals(false, retailerLocation.equals(null));
    }

    @Test
    public void testEqualsSameObj(){
        assertEquals(true, retailerLocation.equals(retailerLocation));
    }

    @Test
    public void testHashCodeSame(){
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
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        int hash1 = retailerLocation.hashCode();
        int hash2 = retailerLocation1.hashCode();
        assertEquals(hash1,hash2);
    }

    @Test
    public void testHashCodeSameButDifferent(){
        name = "Pearl Bodywork";
        addressLine1 = "60 Pearl Street";
        addressLine2 = "Floor 3";
        city = "New York";
        state = "NY";
        zipcode = 10004;
        blockLot = "7-38";
        primaryFunction = "Personal and Professional Services";
        secondaryFunction = "Spa";
        coords = new Point.Float((float) -74.011071, (float) 40.703417);
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        int hash1 = retailerLocation.hashCode();
        int hash2 = retailerLocation1.hashCode();
        assertEquals(hash1,hash2);
    }

    @Test
    public void testHashCodeDifferent(){
        name = "Pearl Bodywor";
        addressLine1 = "60 Pearl Street";
        addressLine2 = "Floor 2";
        city = "New York";
        state = "NY";
        zipcode = 10004;
        blockLot = "7-38";
        primaryFunction = "Personal and Professional Services";
        secondaryFunction = "Spa";
        coords = new Point.Float((float) -74.011071, (float) 40.723417);
        RetailerLocation retailerLocation1 = new RetailerLocation(name, addressLine1, addressLine2, city, state,
                zipcode, blockLot, primaryFunction, secondaryFunction, coords, isUserDefinedPoint);
        int hash1 = retailerLocation.hashCode();
        int hash2 = retailerLocation1.hashCode();
        assertNotEquals(hash1,hash2);
    }




}