package seng202.team1;

/**
 * Class for the retailer locations. Has a single constructor that sets the values for the points.
 * All methods are getters and setters.
 * @author Josh Burt
 */
public class RetailerLocation extends DataPoint {

    private String name;
    private String address;
    private String primaryFunction;
    private String secondaryFunction;

    public RetailerLocation(String name, String address, String primaryFunction, String secondaryFunction) {
        this.name = name;
        this.address = address;
        this.primaryFunction = primaryFunction;
        this.secondaryFunction = secondaryFunction;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


    @Override
    public String toString() {
        return "RetailerLocation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", primaryFunction='" + primaryFunction + '\'' +
                ", secondaryFunction='" + secondaryFunction + '\'' +
                '}';
    }
}