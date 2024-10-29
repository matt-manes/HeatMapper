package utilities;

public class Round {
    /**
     * Round `num` to the given number of places.
     *
     * @param num    The number to round.
     * @param places Exception thrown if this is less than 0.
     * @return The rounded number.
     */
    public static double round(double num, int places) {
        if (places < 0) throw new IllegalArgumentException("`places` must be positive.");
        double scale = Math.pow(10, places);
        double tmp = num * scale;
        // If `tmp` is out of this range and passed to `Math.round`
        // we get either `MIN_VALUE` or `MAX_VALUE` divided by scale back.
        if (tmp <= Long.MIN_VALUE || Long.MAX_VALUE <= tmp) return num;
        return Math.round(num * scale) / scale;
    }
}
