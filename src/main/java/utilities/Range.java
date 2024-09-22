package utilities;

/**
 * Simple helper to hold a number range.
 */
public class Range {

    public Range() {}

    public Range(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double a, b;

    /**
     * @return The difference between b and a.
     */
    public double getDelta() {return b - a;}

    /**
     * @return The absolute value of the difference between b and a.
     */
    public double getAbsDelta() {return Math.abs(b - a);}
}