package utilities;

/**
 * Scale number between to two ranges.
 */
public class Scale {

    /**
     * Scale numbers between two ranges.
     *
     * @param a A range for scaling numbers.
     * @param b A range for scaling numbers.
     */
    public Scale(Range a, Range b) {
        this.a = a;
        this.b = b;
    }

    public Range a, b;

    /**
     * Scale a number from `from` to `to`.
     *
     * @param val  The number to scale.
     * @param from The range `val` is presumed to be on.
     * @param to   The range `val` should be scaled to.
     * @return The scaled value.
     */
    private double scale(double val, Range from, Range to) {
        return (((val - from.a) * to.getDelta()) / from.getDelta()) + to.a;
    }

    /**
     * Scale a number from range `a` to range `b`.
     *
     * @param val The number to scale.
     * @return The scaled value.
     */
    public double fromAToB(double val) {return scale(val, a, b);}

    /**
     * Scale a number from range `b` to range `a`.
     *
     * @param val The number to scale.
     * @return The scaled value.
     */
    public double fromBToA(double val) {return scale(val, b, a);}
}
