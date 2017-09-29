package seng202.team1.Model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.awt.*;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;

/**
 * Class for the retailer locations. Has a single constructor that sets the values for the points.
 * All methods are getters and setters.
 *
 * @author Josh Burt
 */
public class RetailerLocation extends DataPoint implements java.io.Serializable {

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
    private Double distanceFrom; // stores the distance from point

    /**
     * Constructor for RetailerLocation
     *
     * @param name               Name of retailer
     * @param addressLine1       First line of address
     * @param addressLine2       Pre-line of address (e.g. Floor 2)
     * @param city               City retailer is in
     * @param state              State retailer is in
     * @param zipcode            ZIP code retailer is in
     * @param blockLot           block-lot of retailer
     * @param primaryFunction    primary function of retailer
     * @param secondaryFunction  secondary function of retailer
     * @param coords             co-ordinates of retailer in the form (longitude, latitude)
     * @param isUserDefinedPoint true if the retailer is user-defined
     */
    public RetailerLocation(String name, String addressLine1, String addressLine2,
                            String city, String state, int zipcode, String blockLot, String primaryFunction,
                            String secondaryFunction, Point.Float coords, boolean isUserDefinedPoint) {
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
        this.primaryFunction = primaryFunction;
        this.secondaryFunction = secondaryFunction;
        this.coords = coords;
        this.isUserDefinedPoint = isUserDefinedPoint;
    }

    /**
     * Constructor for RetailerLocation without co-ordinates.
     *
     * @param name               Name of retailer
     * @param addressLine1       First line of address
     * @param addressLine2       Pre-line of address (e.g. Floor 2)
     * @param city               City retailer is in
     * @param state              State retailer is in
     * @param zipcode            ZIP code retailer is in
     * @param blockLot           block-lot of retailer
     * @param primaryFunction    primary function of retailer
     * @param secondaryFunction  secondary function of retailer
     * @param isUserDefinedPoint true if the retailer is user-defined
     */
    public RetailerLocation(String name, String addressLine1, String addressLine2, String city, String state,
                            int zipcode, String blockLot, String primaryFunction, String secondaryFunction,
                            boolean isUserDefinedPoint) {
        this(name, addressLine1, addressLine2, city, state, zipcode, blockLot,
                primaryFunction, secondaryFunction, null, isUserDefinedPoint);
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
        return coords.x;
    }

    public void setLongitude(float longitude) {
        this.coords.x = longitude;
    }

    public float getLatitude() {
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
        if (!addressLine2.isEmpty()) {
            //There is a preline
            address += addressLine2 + ", ";
        }
        if (!addressLine1.isEmpty()) {
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
        description += "\nBlock-Lot: " + blockLot;
        description += "\nCo-ordinates: (" + getLatitude() + ", " + getLongitude() + ")";
        description += "\nFunction: " + primaryFunction + " (" + secondaryFunction + ")";

        return description;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        RetailerLocation that = (RetailerLocation) obj;
        return this.name.equalsIgnoreCase(that.name) && this.primaryFunction.equalsIgnoreCase(that.primaryFunction);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(name).
                append(primaryFunction).
                toHashCode();
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
        return escapeEcmaScript(getDescription());


    }
}
