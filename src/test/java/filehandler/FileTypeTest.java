package filehandler;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileTypeTest {

    Path fitPath = Path.of("somefilepath.fit");
    Path fitGZPath = Path.of("somefilepath.fit.gz");
    Path gpxPath = Path.of("somefilepath.gpx");

    @Test
    void hasExt() {
        assertAll(() -> assertTrue(FileType.hasExt(fitPath, ".fit")),
                () -> assertFalse(FileType.hasExt(fitPath, ".txt")));
    }

    @Test
    void hasExts() {
        assertAll(
                () -> assertTrue(FileType.hasExts(fitPath, new String[]{".txt", ".html", ".fit"})),
                () -> assertFalse(
                        FileType.hasExts(fitPath, new String[]{".txt", ".html", ".csv"})));
    }

    @Test
    void isFIT() {
        assertAll(() -> assertTrue(FileType.isFIT(fitPath)),
                () -> assertFalse(FileType.isFIT(fitGZPath)));
    }

    @Test
    void isFITGZ() {
        assertAll(() -> assertTrue(FileType.isFITGZ(fitGZPath)),
                () -> assertFalse(FileType.isFITGZ(fitPath)));
    }

    @Test
    void isGPX() {
        assertAll(() -> assertTrue(FileType.isGPX(gpxPath)),
                () -> assertFalse(FileType.isGPX(fitGZPath)));
    }

    @Test
    void isFITOrFITGZ() {
        assertAll(() -> assertTrue(FileType.isFITOrFITGZ(fitPath)),
                () -> assertTrue(FileType.isFITOrFITGZ(fitGZPath)),
                () -> assertFalse(FileType.isFITOrFITGZ(gpxPath)));
    }
}