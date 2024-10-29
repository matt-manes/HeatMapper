package utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    void round() {
        double num = 4.235188374;
        assertAll(() -> assertEquals(Round.round(num, 2), 4.24),
                () -> assertEquals(Round.round(num, 0), 4),
                () -> assertEquals(Round.round(num, 4), 4.2352),
                () -> assertEquals(Round.round(num, 20), num),
                () -> assertEquals(Round.round(num, 1), 4.2),
                () -> assertEquals(Round.round(-1 * num, 2), -4.24));
    }
}