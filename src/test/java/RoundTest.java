import utilities.Round;

public class RoundTest {
    public static void main(String[] args) {
        double num = 4.235188374;
        assert Round.round(num, 2) == 4.24;
        assert Round.round(num, 0) == 4;
        assert Round.round(num, 1)==4.2;
        assert Round.round(num, 100) == num;
        num *= -1;
        assert Round.round(num, 2) == -4.24;
    }
}
