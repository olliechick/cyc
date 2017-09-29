package seng202.team1;

import org.junit.Before;
import org.junit.Test;
import seng202.team1.Model.WifiPoint;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Created by och26 on 12/09/17.
 */
public class WifiPointTest {

    int objectId;
    Point.Float coords;
    String name;
    String location;     // not as helpful as it suggests, some are addresses some are not
    String locationType;
    String hood;         // neighbourhood
    String borough;
    String city;
    int zipcode;
    String cost;         // e.g. Free, Limited Free
    String provider;
    String remarks;
    String ssid;
    String sourceId;
    LocalDateTime datetimeActivated;
    boolean isUserDefinedPoint;

    WifiPoint wifiPoint;

    @Before
    public void setUp() throws Exception {
        objectId = 998;
        coords = new Point.Float((float) -73.994039, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        wifiPoint = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);


    }

    @Test
    public void testName() {
        assertEquals(wifiPoint.getSsid(), wifiPoint.getName());
    }

    @Test
    public void testGetDescription() {
        String expectedString = "Location: 179 WEST 26 STREET (Outdoor Kiosk) - mn-05-123662, Midtown-Midtown South, Manhattan, New York 10001\n" +
                "Coordinates: (40.745968, -73.99404)\n" +
                "Cost: Free\n" +
                "Source ID: LINK-00869\n" +
                "Activated: 12:00:00 am 18/1/2017\n" +
                "ID: 998\n" +
                "Remarks: Tablet Internet -phone , Free 1 GB Wi-FI Service";
        assertEquals(expectedString, wifiPoint.getDescription());
    }

    @Test
    public void TestEqualsSame(){
        objectId = 998;
        coords = new Point.Float((float) -73.994039, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        WifiPoint wifiPoint1 = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        boolean isSame = wifiPoint.equals(wifiPoint1);
        assertEquals(true, isSame);
    }

    @Test
    public void TestEqualsDifferant(){
        objectId = 998;
        coords = new Point.Float((float) -73.994139, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        WifiPoint wifiPoint1 = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        boolean isSame = wifiPoint.equals(wifiPoint1);
        assertEquals(false, isSame);
    }
    @Test
    public void TestEqualsEqualsButDifferant(){
        objectId = 998;
        coords = new Point.Float((float) -73.994039, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Limited Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        WifiPoint wifiPoint1 = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        boolean isSame = wifiPoint.equals(wifiPoint1);
        assertEquals(true, isSame);
    }

    @Test
    public void TestEqualsNull() {
        boolean isSame = wifiPoint.equals(null);
        assertEquals(false, isSame);
    }

    @Test
    public void TestEqualsSameObj() {
        boolean isSame = wifiPoint.equals(wifiPoint);
        assertEquals(true, isSame);
    }

    @Test
    public void TestHashCodeSame(){
        objectId = 998;
        coords = new Point.Float((float) -73.994039, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        WifiPoint wifiPoint1 = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        int hash1 = wifiPoint.hashCode();
        int hash2 = wifiPoint1.hashCode();
        assertEquals(hash1,hash2);
    }

    @Test
    public void TestHashCodeDifferant() {
        objectId = 998;
        coords = new Point.Float((float) -73.994139, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        WifiPoint wifiPoint1 = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        int hash1 = wifiPoint.hashCode();
        int hash2 = wifiPoint1.hashCode();
        assertNotEquals(hash1,hash2);

    }

    @Test
    public void TestHashCodeSameButDifferant() {
        objectId = 998;
        coords = new Point.Float((float) -73.994039, (float) 40.745968);
        name = "mn-05-123662";
        location = "179 WEST 26 STREET";
        locationType = "Outdoor Kiosk";
        hood = "Midtown-Midtown South";
        borough = "Manhattan";
        city = "New York";
        zipcode = 10001;
        cost = "Limited Free";
        provider = "LinkNYC - Citybridge";
        remarks = "Tablet Internet -phone , Free 1 GB Wi-FI Service";
        ssid = "LinkNYC Free Wi-Fi";
        sourceId = "LINK-00869";
        datetimeActivated = LocalDateTime.of(2017, Month.JANUARY, 18, 0, 0, 0);
        isUserDefinedPoint = false;

        WifiPoint wifiPoint1 = new WifiPoint(objectId, coords, name, location, locationType, hood, borough, city, zipcode,
                cost, provider, remarks, ssid, sourceId, datetimeActivated, isUserDefinedPoint);
        int hash1 = wifiPoint.hashCode();
        int hash2 = wifiPoint1.hashCode();
        assertEquals(hash1,hash2);
    }




}