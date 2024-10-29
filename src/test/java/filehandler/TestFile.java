package filehandler;

import settings.ActivitiesDir;

import java.nio.file.Path;
import java.util.List;

public class TestFile {

    /**
     * @return A gzip file from activities directory.
     */
    static Path getGZFile() {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        // Find a compressed one
        for (Path path : paths) {
            if (FileType.isFITGZ(path)) {
                return path;
            }
        }
        throw new RuntimeException("Could not find a `.fit.gz` file.");
    }

    /**
     * @return A gpx file from activities directory.
     */
    static Path getGPXFile() {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        // Find a compressed one
        for (Path path : paths) {
            if (FileType.isGPX(path)) {
                return path;
            }
        }
        throw new RuntimeException("Could not find a `.gpx` file.");
    }
}
