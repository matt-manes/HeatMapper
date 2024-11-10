package settings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    void load() {
        // If this doesn't throw an error, we passed
        Settings.load();
    }

    @Test
    void dump() {
        Settings.load();
        double origComp = Settings.colorCompression;
        Settings.colorCompression = 0.1;
        Settings.dump();
        Settings.load();
        assertEquals(0.1, Settings.colorCompression);
        Settings.colorCompression = origComp;
        Settings.dump();
        Settings.load();
        assertEquals(origComp, Settings.colorCompression);
    }
}