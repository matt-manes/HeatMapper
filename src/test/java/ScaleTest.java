import utilities.Range;
import utilities.Scale;

public class ScaleTest {
    public static void main(String[] args) {
        Scale scale = new Scale(new Range(0, 1), new Range(-100, 100));
        assert 0 == scale.fromAToB(0.5);
        assert 0.5 == scale.fromBToA(0);
        assert 0 == scale.fromBToA(-100);
        scale.b = new Range(100, -100);
        assert 0 == scale.fromBToA(100);
    }
}
