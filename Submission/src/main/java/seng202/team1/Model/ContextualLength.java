package seng202.team1.Model;

/**
 * Used to sort lengths where a contextualised version is wanted to be displayed,
 * but sorting still works.
 *
 * @author Ollie Chick
 */
public class ContextualLength implements Comparable<ContextualLength>{

    private double length;
    private String lengthString;

    public ContextualLength(double length, String lengthString) {
        this.length = length;
        this.lengthString = lengthString;
    }

    public ContextualLength(int length, String lengthString) {
        this.length = length;
        this.lengthString = lengthString;
    }

    public ContextualLength(long length, String lengthString) {
        this.length = length;
        this.lengthString = lengthString;
    }

    public ContextualLength(float length, String lengthString) {
        this.length = length;
        this.lengthString = lengthString;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getLengthString() {
        return lengthString;
    }

    public void setLengthString(String lengthString) {
        this.lengthString = lengthString;
    }

    public int compareTo(ContextualLength otherLength) {
        return (int) (this.length - otherLength.getLength());
    }

    public String toString() {
        return lengthString;
    }
}
