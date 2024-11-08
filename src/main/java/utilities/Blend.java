package utilities;

public class Blend {
    public static double blend(double val1, double val2, double control) {
        return val1 + (control * (val2 - val1));
    }
}
