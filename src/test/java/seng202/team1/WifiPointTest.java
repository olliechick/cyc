package seng202.team1;

import java.awt.Point;
import java.time.LocalDateTime;
import java.time.Month;

import static junit.framework.Assert.assertEquals;


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

    public void testName() {
        assertEquals(wifiPoint.getSsid(), wifiPoint.getName());
    }

    public void testGetDescription() {
        String expectedString = "Location: 179 WEST 26 STREET - mn-05-123662 (Outdoor Kiosk), Midtown-Midtown South, "
        + "Manhattan, New York 10001 (40.745968, -73.994039)\nCost: Free\nProvider: LinkNYC - Citybridge\n"
        + "SSID: LinkNYC Free Wi-Fi\nSourceID: LINK-008695\nActivated: 12:00:00 am 1/18/2017\nID " +
                "998";
        assertEquals(expectedString, wifiPoint.getDescription());
    }

}