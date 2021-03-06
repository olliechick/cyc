package seng202.team1.Model;

import java.awt.Point;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;

/**
 * Class for the retailer locations. Has a single constructor that sets the values for the points.
 * All methods are getters and setters.
 *
 * @author Josh Burt
 */
public class RetailerLocation extends DataPoint implements java.io.Serializable {

    private String name;
    private String addressLine1 = "";
    private String addressLine2;
    private String city;
    private String state;
    private int zipcode;
    private String blockLot;
    private String primaryFunction;
    private String secondaryFunction;
    private Point.Float coords;
    private Double distanceFrom; // stores the distance from point

    /**
     * Constructor for RetailerLocation
     *
     * @param name              Name of retailer
     * @param addressLine1      First line of address
     * @param addressLine2      Pre-line of address (e.g. Floor 2)
     * @param city              City retailer is in
     * @param state             State retailer is in
     * @param zipcode           ZIP code retailer is in
     * @param blockLot          block-lot of retailer
     * @param primaryFunction   primary function of retailer
     * @param secondaryFunction secondary function of retailer
     * @param coords            co-ordinates of retailer in the form (longitude, latitude)
     */
    public RetailerLocation(String name, String addressLine1, String addressLine2,
                            String city, String state, int zipcode, String blockLot, String primaryFunction,
                            String secondaryFunction, Point.Float coords) {
        this.name = name;

        if (primaryFunction.equalsIgnoreCase("")) {
            this.primaryFunction = "Other";
        } else {
            this.primaryFunction = primaryFunction;
        }
        if (secondaryFunction.equalsIgnoreCase("")) {
            this.secondaryFunction = "Other";
        } else {
            this.secondaryFunction = secondaryFunction;
        }
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.blockLot = blockLot;
        this.coords = coords;
    }

    /**
     * Constructor for RetailerLocation without co-ordinates. Sets coords to null.
     *
     * @param name              Name of retailer
     * @param addressLine1      First line of address
     * @param addressLine2      Pre-line of address (e.g. Floor 2)
     * @param city              City retailer is in
     * @param state             State retailer is in
     * @param zipcode           ZIP code retailer is in
     * @param blockLot          block-lot of retailer
     * @param primaryFunction   primary function of retailer
     * @param secondaryFunction secondary function of retailer
     */
    public RetailerLocation(String name, String addressLine1, String addressLine2, String city, String state,
                            int zipcode, String blockLot, String primaryFunction, String secondaryFunction) {
        this(name, addressLine1, addressLine2, city, state, zipcode, blockLot,
                primaryFunction, secondaryFunction, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }


    /**
     * Used in the table view to display ZIP codes, while treating -1 as null.
     * @return the ZIP code of the retailer. If it is -1, then null is returned.
     */
    public String getZipcodeString() {
        if (zipcode == -1) {
            return null;
        } else {
            return "" + zipcode;
        }
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getBlockLot() {
        return blockLot;
    }

    public void setBlockLot(String blockLot) {
        this.blockLot = blockLot;
    }

    public String getPrimaryFunction() {
        return primaryFunction;
    }

    public void setPrimaryFunction(String primaryFunction) {
        if (primaryFunction.equalsIgnoreCase("")) {
            this.primaryFunction = "Other";
        } else {
            this.primaryFunction = primaryFunction;
        }
    }

    public String getSecondaryFunction() {
        return secondaryFunction;
    }

    public void setSecondaryFunction(String secondaryFunction) {
        if (secondaryFunction.equalsIgnoreCase("")) {
            this.secondaryFunction = "Other";
        } else {
            this.secondaryFunction = secondaryFunction;
        }
    }

    public Point.Float getCoords() {
        return coords;
    }

    public void setCoords(Point.Float coords) {
        this.coords = coords;
    }

    public Double getDistanceFrom() {
        return distanceFrom;
    }

    public void setDistanceFrom(Double distanceFrom) {
        this.distanceFrom = distanceFrom;
    }

    public float getLongitude() {
        if (coords == null) {
            return Float.NEGATIVE_INFINITY; // proxy for null
        }
        return coords.x;
    }

    public void setLongitude(float longitude) {
        this.coords.x = longitude;
    }

    public float getLatitude() {
        if (coords == null) {
            return Float.NEGATIVE_INFINITY; // proxy for null
        }
        return coords.y;
    }

    public void setLatitude(float latitude) {
        this.coords.y = latitude;
    }


    /**
     * Returns the full address of the retailer.
     *
     * @return retailer's address
     */
    public String getAddress() {

        String address = ""; //address to return

        // Check if the address has a line 2
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            //There is a preline
            address += addressLine2 + ", ";
        }
        if (addressLine1 != null && !addressLine1.isEmpty()) {
            //There is a main line
            address += addressLine1 + ", ";
        }
        address += city + ", " + state;
        if (zipcode != -1) {
            address += " " + zipcode;
        }

        return address;
    }


    /**
     * Returns a description of the retailer.
     *
     * @return description of retailer
     */
    public String getDescription() {

        String description = "Address: " + getAddress();
        if (blockLot != null) {
            description += "\nBlock-Lot: " + blockLot;
        }
        if (coords != null) {
            description += "\nCo-ordinates: (" + getLatitude() + ", " + getLongitude() + ")";
        }
        description += "\nFunction: " + primaryFunction + " (" + secondaryFunction + ")";

        return description;
    }


    public void setAllProperties(RetailerLocation newRetailerProperties) {

        this.name = newRetailerProperties.getName();
        this.addressLine1 = newRetailerProperties.getAddressLine1();
        this.addressLine2 = newRetailerProperties.getAddressLine2();
        this.city = newRetailerProperties.getCity();
        this.state = newRetailerProperties.getState();
        this.zipcode = newRetailerProperties.getZipcode();
        this.blockLot = newRetailerProperties.getBlockLot();
        this.primaryFunction = newRetailerProperties.getPrimaryFunction();
        this.secondaryFunction = newRetailerProperties.getSecondaryFunction();
        this.coords = newRetailerProperties.getCoords();
        this.distanceFrom = newRetailerProperties.getDistanceFrom();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RetailerLocation that = (RetailerLocation) o;

        if (!name.equals(that.name)) return false;
        if (!addressLine1.equals(that.addressLine1)) return false;
        if (addressLine2 != null ? !addressLine2.equals(that.addressLine2) : that.addressLine2 != null) return false;
        if (!primaryFunction.equals(that.primaryFunction)) return false;
        return secondaryFunction.equals(that.secondaryFunction);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + addressLine1.hashCode();
        result = 31 * result + (addressLine2 != null ? addressLine2.hashCode() : 0);
        result = 31 * result + primaryFunction.hashCode();
        result = 31 * result + secondaryFunction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RetailerLocation{" +
                "name='" + name + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode=" + zipcode +
                ", blockLot='" + blockLot + '\'' +
                ", primaryFunction='" + primaryFunction + '\'' +
                ", secondaryFunction='" + secondaryFunction + '\'' +
                ", coords=" + coords +
                '}';
    }

    public String toInfoString() {
        return escapeEcmaScript(getName() + "\n" + getDescription());


    }
}
