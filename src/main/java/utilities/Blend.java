package utilities;

public class Blend {
    public static double blend(double a, double b, double control) {
        if (a == b || control == 0) return a;
        if (control == 1) return b;
        return a + (control * (b - a));
    }
}
