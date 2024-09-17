package utilities;

public class Round {
    /**
     * Round `num` to the given number of places.
     *
     * @param num
     * @param places Exception thrown if this is less than 1
     * @return
     */
    public static double round(double num, int places) {
        if (places < 1) throw new IllegalArgumentException("`places` must be greater than 0.");
        return (double) ((int) (num * (10 ^ places))) / (10 ^ places);
    }
}
