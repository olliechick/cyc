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

    private boolean visible = true;
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {

        return visible;
    }

    public boolean isUpdated(boolean update) {
        if (visible != update) {
            visible = update;
            return true;
        } else {
            return false;
        }

    }
}
