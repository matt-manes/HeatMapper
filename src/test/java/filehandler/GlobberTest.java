package filehandler;

import org.junit.jupiter.api.Test;
import settings.ActivitiesDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobberTest {

    @Test
    void glob() {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        assertFalse(paths.isEmpty());
        String[] validExts = new String[]{".fit", ".fit.gz", ".gpx"};
        for (Path path : paths) {
            assertTrue(FileType.hasExts(path, validExts));
        }
    }
}