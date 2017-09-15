package seng202.team1;

import org.junit.Test;

import java.awt.*;
import java.time.LocalDateTime;

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

    }

    @Test
    public void getDescription() throws Exception {
        String expectedString = "ID 998. Location: Outdoor Kiosk - 179 WEST 26 STREET, Midtown-Midtown South, "
        + "Manhattan, New York 10001 (40.745968,-73.994039). Cost: Free. Provider: LinkNYC - Citybridge. "
        + "SSID: LinkNYC Free Wi-Fi. SourceID: LINK-008695. Activated: 12:00:00 am 1/18/2017";

    }

}