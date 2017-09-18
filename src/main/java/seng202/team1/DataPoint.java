package seng202.team1;

/**
 * Holding class for generic arraylist types later. closer to an interface. Probably needs a better name
 * @author Josh Burt
 * @author Ollie Chick
 */
public class DataPoint {

    protected boolean isUserDefinedPoint;

    public boolean isUserDefinedPoint() {
        return isUserDefinedPoint;
    }

    public void setUserDefinedPoint(boolean userDefinedPoint) {
        isUserDefinedPoint = userDefinedPoint;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            //two objects are the same (i.e. pointers to the same thing)
            return true;
        } else {
            //check if string repr is the same
            return this.toString().equals(otherObject.toString());
        }
    }


    /**
     * Returns the name of the data point. This should be overridden.
     * @return "Datapoint name"
     */
    public String getName() {
        return "Datapoint name";
    }


    /**
     * Returns a description of the data point. This should be overridden.
     * @return "A datapoint."
     */
    public String getDescription() {
        return "A datapoint.";
    }
}
