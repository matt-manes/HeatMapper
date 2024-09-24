package utilities;

public class Round {
    /**
     * Round `num` to the given number of places.
     *
     * @param num    The number to round.
     * @param places Exception thrown if this is less than 1.
     * @return The rounded number.
     */
    public static double round(double num, int places) {
        if (places < 0) throw new IllegalArgumentException("`places` must be positive.");
        double scale = Math.pow(10, places);
        return Math.round(num * scale) / scale;
    }
}
