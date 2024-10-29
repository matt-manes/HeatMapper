package utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScaleTest {
    @Test
    void scale() {
        Scale scale = new Scale(new Range(0, 1), new Range(-100, 100));
        assertEquals(0, scale.fromAToB(0.5));
        assertEquals(0.5, scale.fromBToA(0));
        assertEquals(0, scale.fromBToA(-100));
        scale.b = new Range(100, -100);
        assertEquals(0, scale.fromBToA(100));
    }
}