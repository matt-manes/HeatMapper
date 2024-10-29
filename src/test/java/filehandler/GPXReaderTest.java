package filehandler;

import models.Activity;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class GPXReaderTest {

    @Test
    void parseToActivity() {
        Path path = TestFile.getGPXFile();
        Activity activity = GPXReader.parseToActivity(path);
        assertAll(() -> assertNotNull(activity.date()),
                () -> assertTrue(activity.date().after(new Date(0))),
                () -> assertTrue(activity.date().before(Date.from(Instant.now()))));
    }
}