package seng202.team1;

/**
 * Holding class for generic arraylist types later. closer to an interface. Probably needs a better name
 * @author Josh Burt
 * @author Ollie Chick
 */
public class DataPoint {

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
}