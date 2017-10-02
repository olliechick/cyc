package seng202.team1.Model;

/**
 * Holding class for generic arraylist types later. closer to an interface. Probably needs a better name
 *
 * @author Josh Burt
 * @author Ollie Chick
 */
public class DataPoint {

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


    /**
     * Returns the name of the data point. This should be overridden.
     *
     * @return "Datapoint name"
     */
    public String getName() {
        return "Datapoint name";
    }


    /**
     * Returns a description of the data point. This should be overridden.
     *
     * @return "A datapoint."
     */
    public String getDescription() {
        return "A datapoint.";
    }


    /**
     * @param update the point's new visibility
     * @return true if a DataPoint's visible bool has changed
     */
    public boolean isUpdated(boolean update) {
        if (visible != update) {
            visible = update;
            return true;
        } else {
            return false;
        }
    }
}
