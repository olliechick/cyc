package seng202.team1;

import junit.framework.TestCase;

public class RetailerLocationTest extends TestCase {

    public void testGetAddress(){
        RetailerLocation retailerLocation = new RetailerLocation("Pearl " +
                "Bodywork", "60 Pearl Street", "Floor 2", "New York",
                "NY", 10004, "7-38", "Personal and Professional Services",
                "Spa");
        String expectedAddress = "Floor 2, 60 Pearl Street, New York, NY 10004";
        assertEquals(expectedAddress, retailerLocation.getAddress());
    }

    public void testGetAddressNoAddressLine2(){
        RetailerLocation retailerLocation = new RetailerLocation("Candy Plus",
                "16 Beaver Street", "", "New York",
                "NY" , 10004, "11-7", "Shopping",
                "Candy & Chocolate");
        String expectedAddress = "16 Beaver Street, New York, NY 10004";
        assertEquals(expectedAddress, retailerLocation.getAddress());
    }



}