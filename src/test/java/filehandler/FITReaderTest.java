package filehandler;

import models.Activity;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FITReaderTest {

    @Test
    void getFitMessages() {
        Path path = TestFile.getGZFile();
        assertFalse(FITReader.getFitMessages(path).getFileIdMesgs().isEmpty());
    }

    @Test
    void parseToActivity() {
        Path path = TestFile.getGZFile();
        Activity activity = FITReader.parseToActivity(path);
        assertAll(() -> assertNotNull(activity.date()),
                () -> assertTrue(activity.date().after(new Date(0))),
                () -> assertTrue(activity.date().before(Date.from(Instant.now()))));
    }
}