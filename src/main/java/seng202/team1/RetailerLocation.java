package seng202.team1;

import java.awt.*;

/**
 * Class for the retailer locations. Has a single constructor that sets the values for the points.
 * All methods are getters and setters.
 * @author Josh Burt
 */
public class RetailerLocation extends DataPoint {

    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zipcode;
    private String blockLot;
    private String primaryFunction;
    private String secondaryFunction;
    private Point.Float coords;

    public RetailerLocation(String name, String addressLine1, String addressLine2,
                            String city, String state,  int zipcode, String blockLot, String primaryFunction,
                            String secondaryFunction, Point.Float coords, boolean isUserDefinedPoint) {
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.blockLot = blockLot;
        this.primaryFunction = primaryFunction;
        this.secondaryFunction = secondaryFunction;
        this.coords = coords;
        this.isUserDefinedPoint = isUserDefinedPoint;
    }

    /**
    * Overloaded constructor without coords.
     */
    public RetailerLocation(String name, String addressLine1, String addressLine2, String city, String state,
                            int zipcode, String blockLot, String primaryFunction, String secondaryFunction,
                            boolean isUserDefinedPoint) {
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.blockLot = blockLot;
        this.primaryFunction = primaryFunction;
        this.secondaryFunction = secondaryFunction;
        this.isUserDefinedPoint = isUserDefinedPoint;
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
        this.primaryFunction = primaryFunction;
    }

    public String getSecondaryFunction() {
        return secondaryFunction;
    }

    public void setSecondaryFunction(String secondaryFunction) {
        this.secondaryFunction = secondaryFunction;
    }

    public Point.Float getCoords() {
        return coords;
    }

    public void setCoords(Point.Float coords) {
        this.coords = coords;
    }

    /**
    * Returns the full address of the retailer.
     * @return retailer's address
     */
    public String getAddress() {

        String address; //address to return

        // Check if the address has a line 2
        if (addressLine2.isEmpty()) {
            address = addressLine1 + ", " + city + ", " + state + " " + zipcode;
        } else {
            address = addressLine2 + ", " + addressLine1 + ", " + city + ", " + state + " " + zipcode;
        }

        return address;
    }

    /**
     * Returns a description of the retailer.
     * @return description of retailer
     */
    public String getDescription() {

        return String.format("Address: %s\nFunction: %s (%s)", getAddress(), primaryFunction, secondaryFunction);
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
}
