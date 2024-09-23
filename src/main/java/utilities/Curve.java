package utilities;

public class Curve {

    public Curve(double amount, Range range) {
        this.amount = amount;
        valueRange = range;
        scaler = new Scale(valueRange, curveRange);
        calcCoeffs();
    }

    private double amount;
    private Range valueRange;
    private Range curveRange = new Range(0, 1);
    private Scale scaler;
    private double b, c;

    private void calcCoeffs() {
        double min = curveRange.a;
        double max = curveRange.b;
        double divisor = min - (2 * amount) + max;
        double diff = amount - min;
        b = Math.pow(diff, 2) / divisor;
        c = 2 * Math.log((max - amount) / diff);
    }

    public double apply(double val) {
        val = scaler.fromAToB(val);
        val = b * (Math.exp(c * val) - 1);
        return scaler.fromBToA(val);
    }
}
