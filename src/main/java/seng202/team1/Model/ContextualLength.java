package seng202.team1.Model;

/**
 * Used to sort lengths where a contextualised version is wanted to be displayed,
 * but sorting still works.
 *
 * @author Ollie Chick
 */
public class ContextualLength implements Comparable<ContextualLength>{

    private int length;
    private String lengthString;

    public ContextualLength(int length, String lengthString) {
        this.length = length;
        this.lengthString = lengthString;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLengthString() {
        return lengthString;
    }

    public void setLengthString(String lengthString) {
        this.lengthString = lengthString;
    }

    public int compareTo(ContextualLength otherLength) {
        return this.length - otherLength.getLength();
    }

    public String toString() {
        return lengthString;
    }
}
