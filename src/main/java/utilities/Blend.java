package utilities;

public class Blend {
    /**
     * Blends between two values according to a control value.
     *
     * @param a       The value when `control` is 0.
     * @param b       The value when `control` is 1.
     * @param control The control value, excpected to be between 0 and 1.
     * @return The blended value.
     */
    public static double blend(double a, double b, double control) {
        if (a == b || control == 0) return a;
        if (control == 1) return b;
        return a + (control * (b - a));
    }
}
