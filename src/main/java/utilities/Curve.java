package utilities;

/**
 * Apply a taper curve to values to convert from linear to a range of log or exp curves.
 * <p>
 * e.g With a inputs ranging from 0 - 1 and a curve of .9, inputs of 0.1 will be returned as 0.5.
 */
public class Curve {

    /**
     * @param amount A value b/t 0 and 1 exclusive, values of 0.49-0.51 will throw an error
     *               (causes divide by 0).
     * @param range  The expected range of input values.
     */
    public Curve(double amount, Range range) {
        if ((0.49 <= amount && amount <= 0.51) || amount <= 0 || amount >= 1) {
            throw new IllegalArgumentException("Invalid value for `amount`.");
        }

        this.amount = amount;
        scaler = new Scale(range, curveRange);
        calcCoeffs();
    }

    private final double amount;
    private final Range curveRange = new Range(0, 1);
    private final Scale scaler;
    private double b, c;

    private void calcCoeffs() {
        double min = curveRange.a;
        double max = curveRange.b;
        double divisor = min - (2 * amount) + max;
        double diff = amount - min;
        b = Math.pow(diff, 2) / divisor;
        c = 2 * Math.log((max - amount) / diff);
    }

    /**
     * @param val The value to apply the taper curve to.
     * @return The tapered value.
     */
    public double apply(double val) {
        val = scaler.fromAToB(val);
        val = b * (Math.exp(c * val) - 1);
        return scaler.fromBToA(val);
    }
}
